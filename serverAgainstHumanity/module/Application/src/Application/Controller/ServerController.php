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
use Application\Model\Notification;
use Application\Model\NotificationHandler;
use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\XmlRpc\Server;

class ServerController extends AbstractActionController
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
			
		$uid1 = $rpc->getUserId("getzner.e@googlemail.com");
		$uid2 = $rpc->getUserId("egetzner22@gmail.com");
		
		$uids = array($uid1,$uid2);
		//echo $uids;
		echo $uid1;
		echo $uid2;
		
		$rpc->sendNotificationToAll(Notification::notification_new_round_czar,$uids,1);
		
		//echo $server->handle();

		return $this->getResponse();
	}

}
