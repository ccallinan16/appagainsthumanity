<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2013 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application;


//default
use Zend\Mvc\ModuleRouteListener;
use Zend\Mvc\MvcEvent;

//model-related
use Application\Model\PlayedWhiteCard;
use Application\Model\PlayedWhiteCardTable;
use Application\Model\DealtWhiteCard;
use Application\Model\DealtWhiteCardTable;
use Application\Model\DealtBlackCard;
use Application\Model\DealtBlackCardTable;
use Application\Model\Notification;
use Application\Model\NotificationTable;
use Application\Model\Turn;
use Application\Model\TurnTable;
use Application\Model\Participation;
use Application\Model\ParticipationTable;
use Application\Model\Game;
use Application\Model\GameTable;
use Application\Model\User;
use Application\Model\UserTable;
use Zend\Db\ResultSet\ResultSet;
use Zend\Db\TableGateway\TableGateway;


class Module
{
    public function onBootstrap(MvcEvent $e)
    {
        $eventManager        = $e->getApplication()->getEventManager();
        $moduleRouteListener = new ModuleRouteListener();
        $moduleRouteListener->attach($eventManager);
    }

    public function getConfig()
    {
        return include __DIR__ . '/config/module.config.php';
    }

    public function getAutoloaderConfig()
    {
        return array(
//            'Zend\Loader\ClassMapAutoloader' => array(
//                __DIR__ . '/autoload_classmap.php',
//            ),
            'Zend\Loader\StandardAutoloader' => array(
                'namespaces' => array(
                    __NAMESPACE__ => __DIR__ . '/src/' . __NAMESPACE__,
                ),
            ),
        );
    }
    
    public function getServiceConfig()
    {
        return array(
            'factories' => array(
                'Application\Model\UserTable' =>  function($sm) {
                    $tableGateway = $sm->get('UserTableGateway');
                    $table = new UserTable($tableGateway);
                    return $table;
                },
                'UserTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new User());
                    return new TableGateway('user', $dbAdapter, null, $resultSetPrototype);
                },
                'Application\Model\GameTable' =>  function($sm) {
                    $tableGateway = $sm->get('GameTableGateway');
                    $table = new GameTable($tableGateway);
                    return $table;
                },
                'GameTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new Game());
                    return new TableGateway('game', $dbAdapter, null, $resultSetPrototype);
                },
                'Application\Model\ParticipationTable' =>  function($sm) {
                    $tableGateway = $sm->get('ParticipationTableGateway');
                    $table = new ParticipationTable($tableGateway);
                    return $table;
                },
                'ParticipationTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new Participation());
                    return new TableGateway('participation', $dbAdapter, null, $resultSetPrototype);
                },
                'Application\Model\TurnTable' =>  function($sm) {
                    $tableGateway = $sm->get('TurnTableGateway');
                    $table = new TurnTable($tableGateway);
                    return $table;
                },
                'TurnTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new Turn());
                    return new TableGateway('turn', $dbAdapter, null, $resultSetPrototype);
                },
                'Application\Model\NotificationTable' =>  function($sm) {
                    $tableGateway = $sm->get('NotificationTableGateway');
                    $table = new NotificationTable($tableGateway);
                    return $table;
                },
                'NotificationTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new Notification());
                    return new TableGateway('notification', $dbAdapter, null, $resultSetPrototype);
                },
                'Application\Model\DealtBlackCardTable' =>  function($sm) {
                    $tableGateway = $sm->get('DealtBlackCardTableGateway');
                    $table = new DealtBlackCardTable($tableGateway);
                    return $table;
                },
                'DealtBlackCardTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new DealtBlackCard());
                    return new TableGateway('dealt_black_card', $dbAdapter, null, $resultSetPrototype);
                },
                'Application\Model\DealtWhiteCardTable' =>  function($sm) {
                    $tableGateway = $sm->get('DealtWhiteCardTableGateway');
                    $table = new DealtWhiteCardTable($tableGateway);
                    return $table;
                },
                'DealtWhiteCardTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new DealtWhiteCard());
                    return new TableGateway('dealt_white_card', $dbAdapter, null, $resultSetPrototype);
                },              
                'Application\Model\PlayedWhiteCardTable' =>  function($sm) {
                    $tableGateway = $sm->get('PlayedWhiteCardTableGateway');
                    $table = new PlayedWhiteCardTable($tableGateway);
                    return $table;
                },
                'PlayedWhiteCardTableGateway' => function ($sm) {
                    $dbAdapter = $sm->get('Zend\Db\Adapter\Adapter');
                    $resultSetPrototype = new ResultSet();
                    $resultSetPrototype->setArrayObjectPrototype(new PlayedWhiteCard());
                    return new TableGateway('played_white_card', $dbAdapter, null, $resultSetPrototype);
                },          
            ),
        );
    }
    
    
}
