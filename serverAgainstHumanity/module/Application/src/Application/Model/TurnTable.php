<?php
namespace Application\Model;

use Zend\Db\TableGateway\TableGateway;

class TurnTable
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

    public function getTurn($id)
    {
        $id  = (int) $id;
        $rowset = $this->tableGateway->select(array('id' => $id));
        $row = $rowset->current();
        if (!$row) {
            throw new \Exception("Could not find row $id");
        }
        return $row;
    }
    
    public function saveTurn(Turn $turn)
    {
        $data = array(
            'game_id'       => $turn->game_id,
            'roundnumber'   => $turn->roundnumber,
            'user_id'       => $turn->user_id,
            'black_card_id' => $turn->black_card_id,
        );

        $id = (int)$turn->id;
        if ($id == 0) {
            $this->tableGateway->insert($data);
            return $this->tableGateway->lastInsertValue;
        } else {
            if ($this->getTurn($id)) {
                $this->tableGateway->update($data, array('id' => $id));
                return $id;
            } else {
                throw new \Exception('Form id does not exist');
            }
        }
    }

    public function deleteTurn($id)
    {
        $this->tableGateway->delete(array('id' => $id));
    }
    
    public function getNextRoundnumber($gameId) {
        $result = $this->tableGateway->select(array('game_id' => $gameId));
        $currentMaximum = 0;
        foreach($result as $turn) {
          if ($turn->roundnumber > $currentMaximum)
            $currentMaximum = $turn->roundnumber;
        }
        
        return $currentMaximum + 1;
    }
    
    public function getPlayedBlackCards($gameId) {
        $result = $this->tableGateway->select(array('game_id' => $gameId));
        $data = array();
        foreach($result as $turn)
          $data[] = $turn->black_card_id;
        return $data;
    }
}