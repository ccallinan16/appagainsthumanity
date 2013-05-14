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
use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\XmlRpc\Server;

class IndexController extends AbstractActionController
{
    public function indexAction()
    {
    	//$this->_helper->viewRenderer->setNoRenderer();
    	$server = new Server();
    	$rpc = new Rpc($this->getServiceLocator());
    	$server->setClass($rpc, 'cf');
    	
    	echo $rpc->signupUser("testuser8");
    	//echo $server->handle();
		return $this->getResponse();
    }
    
}
