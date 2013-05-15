<?php
namespace Application\Model;

use Zend\Db\TableGateway\TableGateway;

class ParticipationTable
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

    public function getParticipation($id)
    {
        $id  = (int) $id;
        $rowset = $this->tableGateway->select(array('id' => $id));
        $row = $rowset->current();
        if (!$row) {
            throw new \Exception("Could not find row $id");
        }
        return $row;
    }
    
    public function saveParticipation(Participation $participation)
    {
        $data = array(
            'game_id'  => $participation->game_id,
            'user_id'  => $participation->user_id,
            'score'  => $participation->score,
        );

        $id = (int)$participation->id;
        if ($id == 0) {
            $this->tableGateway->insert($data);
            return $this->tableGateway->lastInsertValue;
        } else {
            if ($this->getParticipation($id)) {
                $this->tableGateway->update($data, array('id' => $id));
                return $id;
            } else {
                throw new \Exception('Form id does not exist');
            }
        }
    }

    public function deleteParticipation($id)
    {
        $this->tableGateway->delete(array('id' => $id));
    }
}