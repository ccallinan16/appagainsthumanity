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
        }
    }    
    
    //deal black cards and notify czar
    $this->dealBlackCards($czarId, $gameId);
    $this->rpc->addNotification(Notification::notification_new_round_czar, $czarId, $turnId);    
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
  
  

  
}

