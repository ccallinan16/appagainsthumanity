<?php
namespace Application\Model;

use Zend\Db\TableGateway\TableGateway;

class NotificationTable
{
    protected $tableGateway;

    public function __construct(TableGateway $tableGateway)
    {
        $this->tableGateway = $tableGateway;
    }

    public function fetchAll()
    {
        $resultSet = $this->tableGateway->select();
        return $resultSet;
    }

    public function getNotification($id)
    {
        $id  = (int) $id;
        $rowset = $this->tableGateway->select(array('id' => $id));
        $row = $rowset->current();
        if (!$row) {
            throw new \Exception("Could not find row $id");
        }
        return $row;
    }

    public function saveNotification(Notification $notification)
    {
        $data = array(
            'user_id'    => $notification->user_id,
            'type'       => $notification->type,
            'content_id' => $notification->content_id
        );
                 
        $id = (int)$notification->id;
        if ($id == 0) {
            $this->tableGateway->insert($data);
            return $this->tableGateway->lastInsertValue;
        } else {
            if ($this->getNotification($id)) {
                $this->tableGateway->update($data, array('id' => $id));
                return $id;
            } else {
                throw new \Exception('Form id does not exist');
            }
        }
    }

    public function deleteNotification($id)
    {
        $this->tableGateway->delete(array('id' => $id));
    }
    
    public function getNotificationsOfUser($user_id) {
        $result = $this->tableGateway->select(array('user_id' => $user_id));
        $data = array();
        foreach($result as $notification)
          $data[$notification->id] = $notification->type;
        return $data;
    }
}