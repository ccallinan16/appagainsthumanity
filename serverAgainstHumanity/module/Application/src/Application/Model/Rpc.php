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
    protected $playedWhiteCardTable;
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
    
    public function getPlayedWhiteCardTable()
    {
        if (!$this->playedWhiteCardTable) {
            $this->playedWhiteCardTable = $this->sm->get('Application\Model\PlayedWhiteCardTable');
        }
        return $this->playedWhiteCardTable;
    }
    
    
    public function addAndSendNotificationToAll($type, $userIdList, $contentId)
    {
    	foreach ($userIdList as $userId)
    	{
    		$this->addNotification($type, $userId, $contentId);
    	}
    	
    	//and send.
    	$this->sendNotificationToAll($type, $userIdList, $contentId);
    }
    
    
    public function addNotification($type, $userId, $contentId) {
        $notification = new Notification();
        $notification->setId(0);
        $notification->setType($type);
        $notification->setUserId($userId);      
        $notification->setContentId($contentId);
        $this->getNotificationTable()->saveNotification($notification);
        
    }

    private function sendNotificationToRegids($type, $registration_ids, $contentId)
    {
    	switch($type) {
    		 
    		case Notification::notification_new_game :
    			$submessage = "A new game has started.";
    			break;
    		case Notification::notification_new_round :
    			$submessage = "A new round has started.";
    			break;
    		case Notification::notification_new_round_czar :
    			$submessage = "You get to choose a black card!";
    			break;
    		case Notification::notification_chosen_black :
    			$submessage = "Choose a white card.";
    			break;
    		case Notification::notification_chosen_white :
    			$submessage = "All white cards have been chosen.";
    			break;
    		case Notification::notification_chosen_winner :
    			$submessage = "A winner has been selected!";
    			break;
    		case Notification::notification_end_game :
    			$submessage = "A game has finished. Go to the options to delete.";
    			break;    	
    		default:
    			$submessage = "User Interaction Required.";
    			break;
    	}
    	
    	$message = array("price" => $submessage,
    			"type" => $type,
    			"contentId" => $contentId);
    	
    	$gcmhandler = new Gcm();
    	
    	$gcmhandler->send_notification($registration_ids, $message);
    }
    
  	/**
	 * sends a gcm notification
	 *  
  	 * @param int $userId
  	 * @param int $type
  	 * @param int $contentId
  	 * @return bool true if a regid is set
  	 */
    public function sendNotification($type, $userId, $contentId){
    	
    	//if ($userId == null || $contentId == null)
    	//	return; 
    	
    	$user = $this->getUserTable()->getUser($userId);
    	$gcm = $user->gcm_id;
    	
    	if ($gcm != null && $gcm != "")
    	{
    		 $regId = $gcm;
    	}
    	else
    		return false; //if we don't have a valid id, there is no point in sending it.
    	    	    	     	    	
    	$registration_ids = array($regId);
    	 
    	$this->sendNotificationToRegids($type, $registration_ids, $contentId);
    	return true;
    }
    
    public function sendNotificationToAll($type, $userIdList, $contentId){
    	 
    	$registration_ids = array();
    	 
		foreach($userIdList as $userId )
		{
			$user = $this->getUserTable()->getUser($userId);
			$gcm = $user->gcm_id;
			
			if ($gcm != null && $gcm != "")
			{
				$registration_ids[] = $gcm;
			}
				
		}
    
    	$this->sendNotificationToRegids($type, $registration_ids, $contentId);
    	return true;
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
  	 * checks if the user is registered with a regid
  	 *
  	 * @param string $username
  	 * @return bool true if a regid is set
  	 */
  	public function isRegistedIdSet($username)
  	{
  		$id = $this->getUserTable()->getUserId($username);
  		
  		if ($id > 0)
  		{
  			//user exists
  			$user = $this->getUserTable()->getUser($id);
  			$gcm = $user->gcm_id;
  			
  			if ($gcm != null && $gcm != "")
  				return true;
  		}
  		
  		return false;
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
  	 * retrieves id of existing user or adds new user and returns id
  	 *
  	 * @param string $username
  	 * @param string $gcmid
  	 * @return int id of new or existing user
  	 */
  	public function registerUser($username,$gcmid)
	{	
		$id = $this->getUserTable()->getUserId($username);
		
		//entry already exists, but we should probably add the gcmid.
		if ($id > 0)
		{
			$user = $this->getUserTable()->getUser($id);
		}
		else
		{			
			$user = new User();
			$user->setId(0);
			$user->setUsername($username);
		}
		
		$user->setGCMID($gcmid);
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
  		
        //validate users
        try {
          $user = $this->getUserTable()->getUser($user_id);
        } catch (\Exception $e) {
          throw new \Exception('Invalid user id supplied: ' . $user_id);
        }
        foreach($data['invites'] as $userId) {
          try {
              $invite = $this->getUserTable()->getUser($userId);
          } catch (\Exception $e) {
            throw new \Exception('Invalid user id supplied: ' . $userId);
          }
        }
        
        //validate parameters according to rulebook
        $rulebook = Rulebook::createRulebook($this);
        $rulebook->validateGameData($data);
        
        //create new game
        $game = new Game();
        $game->setId(0);
        $game->setRoundcap($data['roundcap']);
        $game->setScorecap($data['scorecap']);       
        $game->setWinner(0); 
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
                 
        $userList = $data['invites'];
        $userList[] = $user_id;
        $this->addAndSendNotificationToAll(Notification::notification_new_game, $userList, $game_id);
                
        //call onCreate of rulebook
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
        try {
          $turn = $this->getTurnTable()->getTurn($turn_id);
        } catch (\Exception $e) {
          //invalid turn id
          throw new \Exception('Invalid turn id supplied: ' . $turn_id);
        }
        
        //check validity of user id
        if ($turn->user_id != $user_id)
          throw new \Exception('Invalid user for this operation: ' . $user_id);
          
        //check validity of card id
        if (!$this->getDealtBlackCardTable()->isDealtBlackCard($turn->game_id, $card_id, $user_id))
          throw new \Exception('Invalid card id for this operation: ' . $card_id);
          
        //update 
        $turn->setBlackCardId($card_id);
        $this->getTurnTable()->saveTurn($turn);
        
        //remove from dealtBlackCards
        $this->getDealtBlackCardTable()->removeDealtBlackCard($turn->game_id, $card_id, $user_id);
        
        //call onBlackCardChosen of rulebook
        $rulebook = Rulebook::createRulebook($this);
        $rulebook->onBlackCardChosen($user_id, $turn_id, $card_id);
         
        //add notification
        $participants = $this->getParticipationTable()->getParticipants($turn->game_id);
        $this->addAndSendNotificationToAll(Notification::notification_chosen_black, $participants, $turn_id);
                
        //return true if no exception occured
        return true;
  	}
    
    /**
  	 * sets the played white card id of for a specified turn for a specified user
  	 *
     * @param int $user_id
     * @param int $turn_id
     * @param int $card_id          
  	 * @return bool success
  	 */
  	public function chooseWhiteCard($user_id, $turn_id, $card_id)
  	{                   
        //retrieve turn object     
        try {
          $turn = $this->getTurnTable()->getTurn($turn_id);
        } catch (\Exception $e) {
          //invalid turn id
          throw new \Exception('Invalid turn id supplied: ' . $turn_id);
        }
                
        //check validity of user id
        $participants = $this->getParticipationTable()->getParticipants($turn->game_id);
        if ($user_id == $turn->user_id || !in_array($user_id, $participants))
          throw new \Exception('Invalid user for this operation: ' . $user_id);
          
        //check validity of card id
        if (!$this->getDealtWhiteCardTable()->isDealtWhiteCard($turn->game_id, $card_id, $user_id))
          throw new \Exception('Invalid card id for this operation: ' . $card_id);
          
        //add entry in playedWhiteCardTable
        $card = new PlayedWhiteCard();
        $card->setTurnId($turn_id);
        $card->setUserId($user_id);
        $card->setWhiteCardId($card_id);
        $card->setWon(false);
        $this->getPlayedWhiteCardTable()->savePlayedWhiteCard($card);
        
        //remove from dealtWhiteCards
        $this->getDealtWhiteCardTable()->removeDealtWhiteCard($turn->game_id, $card_id, $user_id);
        
        //call onWhiteCardChosen of rulebook
        $rulebook = Rulebook::createRulebook($this);
        $rulebook->onWhiteCardChosen($user_id, $turn_id, $card_id);
         
        //add notification
        $this->addAndSendNotificationToAll(Notification::notification_chosen_white, $participants, $turn_id);
                
        //return true if no exception occured
        return true;
  	}
    
    /**
  	 * sets the won flag of a specified white card for a specified turn
  	 *
     * @param int $user_id
     * @param int $turn_id
     * @param int $card_id          
  	 * @return bool success
  	 */
  	public function chooseWinnerCard($user_id, $turn_id, $card_id)
  	{                   
        //retrieve turn object
        try {
          $turn = $this->getTurnTable()->getTurn($turn_id);
        } catch (\Exception $e) {
          //invalid turn id
          throw new \Exception('Invalid turn id supplied: ' . $turn_id);
        }
                
        //check validity of user id
        if ($turn->user_id != $user_id)
          throw new \Exception('Invalid user for this operation: ' . $user_id);
          
        //check validity of card id
        if (!$this->getPlayedWhiteCardTable()->isPlayedWhiteCard($turn_id, $card_id))
          throw new \Exception('Invalid card id for this operation: ' . $card_id);
          
        //add entry in playedWhiteCardTable
        $card = $this->getPlayedWhiteCardTable()->getPlayedWhiteCardByTurnAndCard($turn_id, $card_id);
        $card->setWon(true);
        $this->getPlayedWhiteCardTable()->savePlayedWhiteCard($card);
        
        //add notification
        $participants = $this->getParticipationTable()->getParticipants($turn->game_id);
        $this->addAndSendNotificationToAll(Notification::notification_chosen_winner, $participants, $turn_id);
        
        //call onWhiteCardChosen of rulebook
        $rulebook = Rulebook::createRulebook($this);
        $rulebook->onWinnerCardChosen($user_id, $turn_id, $card_id);
        
        //return true if no exception occured
        return true;
  	}
}
