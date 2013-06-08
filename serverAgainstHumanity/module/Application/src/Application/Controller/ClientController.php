<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2013 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\XmlRpc\Client;

class ClientController extends AbstractActionController
{
    public function indexAction()
    {
    	$client = new Client('http://localhost/');
        try {
            $data = $client->call('aah.checkConnection');
            $this->layout( 'layout/xml-layout' );
            
			return new ViewModel(array(
            	'data' => $data
        	));

        } catch (Zend_XmlRpc_Client_HttpException $e) {
            require_once 'Zend/Exception.php';
            throw new Zend_Exception($e);
        } catch (Zend_XmlRpc_Client_FaultException $e) {
            require_once 'Zend/Exception.php';
                throw new Zend_Exception($e);
        }
    }
    
    public function creategameAction()
    {
    	$client = new Client('http://10.0.0.3/');
        try {
            $user_id = 14;
            $data = array(
              "roundcap" => 5,
              "scorecap" => 5,
              "invites" => array(
                "1" => 1,
                "5" => 5,
              )
            );
              
            
        
            $data = $client->call('aah.createGame', array((int) $user_id, (array) $data));
            $this->layout( 'layout/xml-layout' );
            
			return new ViewModel(array(
            	'data' => $data
        	));

        } catch (Zend_XmlRpc_Client_HttpException $e) {
            require_once 'Zend/Exception.php';
            throw new Zend_Exception($e);
        } catch (Zend_XmlRpc_Client_FaultException $e) {
            require_once 'Zend/Exception.php';
                throw new Zend_Exception($e);
        }
    }
    
}
