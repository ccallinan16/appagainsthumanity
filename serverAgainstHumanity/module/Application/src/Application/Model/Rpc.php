<?php
namespace Application\Model;

//use Application/Model/UserTable;

class Rpc
{
    const BLACK_MAX_ID = 75;
    const WHITE_MAX_ID = 459;
    const BLACK_HAND_SIZE = 5;
    const WHITE_HAND_SIZE = 10;


	  protected $userTable;
    protected $gameTable;
    protected $participationTable;
    protected $turnTable;
    protected $notificationTable;
    protected $dealtBlackCardTable;
    protected $sm; //serviceLocator
    
    public function __construct($serviceLocator) {
        $this->sm = $serviceLocator;    
    }
    
    /*
     *  table getters
     */
    
    public function getUserTable()
    {
        if (!$this->userTable) {
            $this->userTable = $this->sm->get('Application\Model\UserTable');
        }
        return $this->userTable;
    }
    
    public function getGameTable()
    {
        if (!$this->gameTable) {
            $this->gameTable = $this->sm->get('Application\Model\GameTable');
        }
        return $this->gameTable;
    }
    
    public function getParticipationTable()
    {
        if (!$this->participationTable) {
            $this->participationTable = $this->sm->get('Application\Model\ParticipationTable');
        }
        return $this->participationTable;
    }
    
    public function getTurnTable()
    {
        if (!$this->turnTable) {
            $this->turnTable = $this->sm->get('Application\Model\TurnTable');
        }
        return $this->turnTable;
    }
    
    public function getNotificationTable()
    {
        if (!$this->notificationTable) {
            $this->notificationTable = $this->sm->get('Application\Model\NotificationTable');
        }
        return $this->notificationTable;
    }
    
    public function getDealtBlackCardTable()
    {
        if (!$this->dealtBlackCardTable) {
            $this->dealtBlackCardTable = $this->sm->get('Application\Model\DealtBlackCardTable');
        }
        return $this->dealtBlackCardTable;
    }
    
    /*
     *  private helper functions
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
        
        //add turn notifications            
        foreach($participants as $userId)
            $this->addNotification(Notification::notification_new_round, $userId, $turnId);
            
        //select black cards for czar
          //retrieve already played cards
        $playedBlackCards = $this->getTurnTable()->getPlayedBlackCards($gameId);
          //choose new cards
        $cards = array();
        while (count($cards) < Rpc::BLACK_HAND_SIZE) {
          $blackCardId = rand(1, Rpc::BLACK_MAX_ID);
          if (! in_array($blackCardId, $playedBlackCards) &&
              ! in_array($blackCardId, $cards))
            $cards[] = $blackCardId;
        }
          //enter new cards into table
        $this->getDealtBlackCardTable()->dealCards($gameId, $czarId, $cards);
          //notify czar
        $this->addNotification(Notification::notification_choose_black, $czarId, $gameId);    
    }
    
    private function addNotification($type, $userId, $contentId) {
        $notification = new Notification();
        $notification->setId(0);
        $notification->setType($type);
        $notification->setUserId($userId);      
        $notification->setContentId($contentId);
        $this->getNotificationTable()->saveNotification($notification);
    }
    
    /*
     *  public rpc functions
     */

    /**
     * Test method wich only returns true
     *
     * @return bool
     */
    public function checkConnection()
    {  	
    	 return true;
    }
    
  	/**
  	 * retrieves id of user having the supplied username
  	 *
  	 * @param string $username
  	 * @return int id of user or 0 if user does not exist
  	 */
  	public function getUserId($username)
  	{    
  	    return $this->getUserTable()->getUserId($username);
  	}
  
    /**
  	 * retrieves id of existing user or adds new user and returns id
  	 *
  	 * @param string $username
  	 * @return int id of new or existing user
  	 */
  	public function signupUser($username)
  	{    
        //check if user exists
        $id = $this->getUserTable()->getUserId($username);
        if ($id > 0)
          return $id;
          
        //otherwise add new user
        $user = new User();
        $user->setId(0);
        $user->setUsername($username);

  	    return $this->getUserTable()->saveUser($user);
  	}
    
    /**
  	 * adds a new game
  	 *
     * @param int $user_id
     * @param struct|array $data          
  	 * @return bool success
  	 */
  	public function createGame($user_id, $data)
  	{           
        //create new game
        $game = new Game();
        $game->setId(0);
        $game->setRoundcap($data['roundcap']);
        $game->setScorecap($data['scorecap']);        
        $game_id = $this->getGameTable()->saveGame($game);
        
        //add participation of creating player
        $participation = new Participation();
        $participation->setId(0);
        $participation->setGameId($game_id);  
        $participation->setUserId($user_id);
        $participation->setScore(0);
        $this->getParticipationTable()->saveParticipation($participation);
        
        //add participation of invited users
        foreach($data['invites'] as $userId) {
            $participation->setUserId($userId);
            $this->getParticipationTable()->saveParticipation($participation);
        }
        
        //add game notification
          //game creator
        $this->addNotification(Notification::notification_new_game, $user_id, $game_id);  
          //invited users
        foreach($data['invites'] as $userId)
            $this->addNotification(Notification::notification_new_game, $userId, $game_id);     

        //add new turn
        $this->addTurn($game_id);
        
        //return true if no exception occured
        return true;
  	}
}