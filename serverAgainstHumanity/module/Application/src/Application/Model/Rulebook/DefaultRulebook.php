<?php
namespace Application\Model\Rulebook;

use Application\Model\Turn;
use Application\Model\Notification;
use Application\Model\DealtBlackCard;
use Application\Model\DealtWhiteCard;

class DefaultRulebook extends Rulebook {
 
  //constants related to this game-type
  const BLACK_HAND_SIZE = 5;
  const WHITE_HAND_SIZE = 10;
  const MIN_PLAYER = 1;
 
  public function __construct($new_rpc) {
    parent::__construct($new_rpc);
  }
  
  /*
   *  private helper functions
   */

  /**
   * Adds a new turn for the given game id     
   */     
  private function addTurn($gameId) {
    //retrieve participants
    $participants = $this->getParticipationTable()->getParticipants($gameId);    
   
    //retrieve last turn number
    $roundnumber = $this->getTurnTable()->getNextRoundnumber($gameId);
    
    //create new turn
    $turn = new Turn();
    $turn->setId(0);
    $turn->setGameId($gameId);
    $turn->setRoundnumber($roundnumber);
      //select czar for turn        
    $key = array_rand($participants, 1);
    $czarId = $participants[$key];
    $turn->setUserId($czarId);
    $turn->setBlackCardId(0);
      //add turn to db
    $turnId = $this->getTurnTable()->saveTurn($turn);
        
    //deal white cards and notify players        
    foreach($participants as $userId) {
        if ($userId != $czarId) {
            $this->dealWhiteCards($userId, $gameId);
            $this->rpc->addNotification(Notification::notification_new_round, $userId, $turnId);
            //do not gcm-notify these people, as there is nothing to do for them yet.
        }
    }       
    
    //deal black cards and notify czar
    $this->dealBlackCards($czarId, $gameId);
    $this->rpc->addNotification(Notification::notification_new_round_czar, $czarId, $turnId);
    $this->rpc->sendNotification(Notification::notification_new_round_czar, $czarId, $turnId);
  }
  
  public function dealWhiteCards($user_id, $game_id) {
    //retrieve already played cards
    $dealtWhiteCards = $this->getDealtWhiteCardTable()->getDealtWhiteCardsOfGame($game_id);
    
    //choose new cards
    $cards = array();
    while (count($cards) < DefaultRuleBook::WHITE_HAND_SIZE) {
      $whiteCardId = rand(1, RuleBook::WHITE_MAX_ID);
      if (! in_array($whiteCardId, $dealtWhiteCards) &&
          ! in_array($whiteCardId, $cards))
        $cards[] = $whiteCardId;
    }
    
    //update dealt cards
    $this->getDealtWhiteCardTable()->dealCards($game_id, $user_id, $cards);
  }
  
  public function dealBlackCards($user_id, $game_id) {
    //retrieve already played cards
    $playedBlackCards = $this->getTurnTable()->getPlayedBlackCards($game_id);
    //choose new cards
    $cards = array();
    while (count($cards) < DefaultRuleBook::BLACK_HAND_SIZE) {
      $blackCardId = rand(1, RuleBook::BLACK_MAX_ID);
      if (! in_array($blackCardId, $playedBlackCards) &&
          ! in_array($blackCardId, $cards))
        $cards[] = $blackCardId;
    }
    
    //update dealt cards
    $this->getDealtBlackCardTable()->dealCards($game_id, $user_id, $cards);
  }

  /**
   * called before game creation to validate provided game data
   *     
   * @param array $data contains game data
   * @return void      
   */   
    public function validateGameData($data) {
      
      //check that at least 2 players were invited
      if (!array_key_exists('invites', $data) || count($data['invites']) < DefaultRulebook::MIN_PLAYER)
        throw new \Exception('Insufficient number of players invited');
      
      //either roundcap or scorecap need to be > 0 
      //both need to be >= 0
      if (!array_key_exists('roundcap', $data) || !array_key_exists('scorecap', $data) ||
          $data['roundcap'] < 0 || $data['scorecap'] < 0 ||
          $data['roundcap'] == 0 && $data['scorecap'] == 0)
        throw new \Exception('Invalid game parameters supplied');
    }
  
  /**
   * OnEvent-Functions
   */     
  
  /**
   * called after newly created game and participation entries are already added
   *     
   * @param int $game_id id of newly created game
   * @param int $user_id id of user who created the game
   * @return void      
   */         
  public function onCreateGame($game_id, $user_id) {
    $this->addTurn($game_id);
  }

  /**
   * called after black card entry in turn is updated and card id is removed in dealt black card table
   *  
   * @param int $user_id      
   * @param int $turn_id
   * @param int $card_id
   * @return void      
   */   
  public function onBlackCardChosen($user_id, $turn_id, $card_id) {
    //get turn object
    $turn = $this->getTurnTable()->getTurn($turn_id);
  
    //drop remaining dealt black cards for czar
    $this->getDealtBlackCardTable()->dropDealtCards($turn->game_id, $user_id);
  }
  
  /**
   * called after white card was inserted into playedWhiteCard table
   *  
   * @param int $user_id      
   * @param int $turn_id
   * @param int $card_id
   * @return void      
   */   
  public function onWhiteCardChosen($user_id, $turn_id, $card_id) {
    //do nothing for now
  }
  
  /**
   * called after white card was inserted into playedWhiteCard table
   *  
   * @param int $user_id      
   * @param int $turn_id
   * @param int $card_id
   * @return void      
   */   
  public function onWinnerCardChosen($user_id, $turn_id, $card_id) {
    //increment score of user who played the winning card
    $card = $this->getPlayedWhiteCardTable()->getPlayedWhiteCardByTurnAndCard($turn_id, $card_id);
    $turn = $this->getTurnTable()->getTurn($turn_id);
    $participation = $this->getParticipationTable()->getParticipationByGameAndUser($turn->game_id, $card->user_id);
    $participation->score = $participation->score + 1;
    $this->getParticipationTable()->saveParticipation($participation);
    
    //check if score cap or turn cap was reached
    $game = $this->getGameTable()->getGame($turn->game_id);
    if ($participation->score >= $game->scorecap || $turn->roundnumber >= $game->roundcap) {
      //check if we are in a draw state
      $participationData = $this->getParticipationTable()->getParticipationOfGame($turn->game_id);
      //define custom compare function to sort participation by score
      usort($participationData, function($a, $b) {
          return $b['score'] - $a['score'];
      });
      //if first two entries have the same score we have a draw end need to play another round
      if ($participationData[0]['score'] == $participationData[1]['score']) {
        $this->addTurn($game->id);
      } else {
        //update game and set winner id to first entry of participationData
        $game = $this->getGameTable()->getGame($turn->game_id);
        $game->setWinner($participationData[0]['user_id']);
        $this->getGameTable()->saveGame($game);
        
        //notify users
        $participants = $this->getParticipationTable()->getParticipants($game->id);
        $this->rpc->addAndSendNotificationToAll(Notification::notification_end_game, $participants, $game->id);
      }
    } else {
      //add new turn
      $this->addTurn($game->id);
    }
  }
  

  
  

  
}

