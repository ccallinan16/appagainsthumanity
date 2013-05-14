<?php
namespace Application\Model;

//use Application/Model/UserTable;

class Rpc
{
	protected $userTable;
    protected $sm; //serviceLocator
    
    public function __construct($serviceLocator) {
      $this->sm = $serviceLocator;    
    }

    /**
     * Test method
     *
     * @return string
     */
    public function test()
    {
    	$users = $this->getUserTable()->fetchAll();
    	$string = "";
    	foreach($users as $user) {
    		$string .= " " . $user->id . " " . $user->username . "\n";
    	}
    	
    	return $string;
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
        $user->setUsername($username);
        $user->setId(0);
  	    $this->getUserTable()->saveUser($user);
        return $this->getUserTable()->getUserId($username);
  	}
    
    public function getUserTable()
    {
        if (!$this->userTable) {
            $this->userTable = $this->sm->get('Application\Model\UserTable');
        }
        return $this->userTable;
    }
}