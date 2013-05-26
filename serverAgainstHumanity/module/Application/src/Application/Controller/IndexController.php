<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2013 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application\Controller;

use Application\Model\Rpc;
use Application\Model\Gcm;
use Application\Model\NotificationHandler;
use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\XmlRpc\Server;

class IndexController extends AbstractActionController
{
    const NAMESPACE_RPC           = "aah";
    const NAMESPACE_NOTIFICATION  = "notification";

    public function indexAction()
    {
    	$server = new Server();
    	//$gcm = new GCM();
    	$rpc = new Rpc($this->getServiceLocator());
    	$server->setClass($rpc, IndexController::NAMESPACE_RPC);
	$notificationHandler = new NotificationHandler($this->getServiceLocator());
	$server->setClass($notificationHandler, IndexController::NAMESPACE_NOTIFICATION);
      
    	
	$username = "testuser";
	$data = array(
		"roundcap" => 5,
		"scorecap" => 5,
		"invites" => array(
			"5" => 5,
			"7" => 7,
			"8" => 8  
		)
	);
      
	    //echo "hello!";
	    //echo $rpc->registerUser($username,123);
	    //print " FieldCount: ".$rpc->getUserTable()->fetchAll()->getFieldCount();
	    //print "\n User: ".$rpc->getUserTable()->fetchAll()->current();
	    
      //echo $rpc->checkConnection();
    	//echo $rpc->createGame(14, $data);
    	//print_r( $notificationHandler->getNotifications(14));
      //print_r( $notificationHandler->getUpdate(155));
      	
		echo $server->handle();
	
		return $this->getResponse();
    }
    
}
