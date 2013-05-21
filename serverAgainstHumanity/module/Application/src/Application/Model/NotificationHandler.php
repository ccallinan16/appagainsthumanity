<?php
namespace Application\Model;

class NotificationHandler
{
	  protected $userTable;
    protected $gameTable;
    protected $participationTable;
    protected $turnTable;
    protected $notificationTable;
    protected $dealtBlackCardTable;
    protected $sm; //serviceLocator
    
    const notification_new_game      = 0;  //important notification
    const notification_new_round     = 1;  //important notification
    const notification_choose_black  = 2;  //important notification
    const notification_chosen_black  = 3;
    const notification_choose_white  = 4;  //important notification
    const notification_chosen_white  = 5;
    const notification_choose_winner = 6;  //important notification
    const notification_chosen_winner = 7;  //important notification
    
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
    
    private function getNewGameUpdate($user_id, $game_id) {
      //fetch data      
        //get game data
      $game = $this->getGameTable()->getGame($game_id);
      $gameData = $game->toArray();
        //get participation data
      $participationData = $this->getParticipationTable()->getParticipationOfGame($game_id);
        //get user data
      $userData = array();
      foreach($participationData as $entry) {
        $userId = $entry['user_id'];
        $user = $this->getUserTable()->getUser($userId);
        $userData[] = $user->toArray();
      }
      
      //prepare data array
      $data = array();
      $data['game'] = $gameData;
      $data['user'] = $userData;
      $data['participation'] =  $participationData;
      
      return $data;
    }
    
    private function getNewRoundUpdate($user_id, $turn_id) {
      //fetch and return data      
      $turn = $this->getTurnTable()->getTurn($turn_id);
      return $turn->toArray();
    }
    
    private function getChooseBlackUpdate($user_id, $game_id) {
      //fetch and return data      
      return $this->getDealtBlackCardTable()->getDealtBlackCards($user_id, $game_id);
    }
    
    /*
     *  public rpc functions
     */
    
    /**
  	 * retrieves the currently available notifications for a given user
  	 *
     * @param int $user_id      
  	 * @return struct|nil notification informations
  	 */
  	public function getNotifications($user_id)
  	{           
        $data = $this->getNotificationTable()->getNotificationsOfUser($user_id);    
        return $data;
  	}
    
    /**
  	 * retrieves the data of the provided notification id
  	 *
     * @param int $notification_id      
  	 * @return array|struct notification informations
  	 */
  	public function getUpdate($notification_id)
  	{           
        $notification = $this->getNotificationTable()->getNotification($notification_id);
        
        switch($notification->type) {
        
          case Notification::notification_new_game :
            $data = $this->getNewGameUpdate($notification->user_id, $notification->content_id);
            break;
          case Notification::notification_new_round :
            $data = $this->getNewRoundUpdate($notification->user_id, $notification->content_id);
            break;    
          case Notification::notification_choose_black :
            $data = $this->getChooseBlackUpdate($notification->user_id, $notification->content_id);
            break;              
                
          
          
          default:
            //something went wrong here..
            break;
        }
        
        
        //$this->getNotificationTable()->deleteNotification($notification_id); 
        return $data;
  	}
}