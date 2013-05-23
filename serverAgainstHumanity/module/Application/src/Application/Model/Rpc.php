<?php
namespace Application\Model;

use Application\Model\Rulebook\Rulebook;

class Rpc
{

	  protected $userTable;
    protected $gameTable;
    protected $participationTable;
    protected $turnTable;
    protected $notificationTable;
    protected $dealtBlackCardTable;
    protected $dealtWhiteCardTable;
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
    
    public function getDealtWhiteCardTable()
    {
        if (!$this->dealtWhiteCardTable) {
            $this->dealtWhiteCardTable = $this->sm->get('Application\Model\DealtWhiteCardTable');
        }
        return $this->dealtWhiteCardTable;
    }
    
    public function addNotification($type, $userId, $contentId) {
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

        //call onCreate of gametype
        $rulebook = Rulebook::createRulebook($this);
        $rulebook->onCreateGame($game_id, $user_id);
        
        //return true if no exception occured
        return true;
  	}
    
    /**
  	 * sets the black card id of an existing turn to the provided card id
  	 *
     * @param int $user_id
     * @param int $turn_id
     * @param int $card_id          
  	 * @return bool success
  	 */
  	public function chooseBlackCard($user_id, $turn_id, $card_id)
  	{                   
        //retrieve turn object
        $turn = $this->getTurnTable()->getTurn($turn_id);
        
        //check validity of user id
        if ($turn->user_id != $user_id)
          return false;
          
        //check validity of card id
        if (!$this->getDealtBlackCardTable()->isDealtBlackCard($turn->game_id, $card_id, $user_id))
          return false;
          
        //update 
        $turn->setBlackCardId($card_id);
        $this->getTurnTable()->saveTurn($turn);
        
        //add notification
        $participants = $this->getParticipationTable()->getParticipants($turn->game_id);
        foreach ($participant as $participant_id) {
          $this->addNotification(Notification::notification_chosen_black, $participant_id, $gameId); 
        }
        
        //call onCreate of gametype
        $rulebook = Rulebook::createRulebook($this);
        $rulebook->onBlackCardChosen($user_id, $turn_id, $card_id);        
        
        //return true if no exception occured
        return true;
  	}
}