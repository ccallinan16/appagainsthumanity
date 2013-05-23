<?php
namespace Application\Model;

use Zend\Db\TableGateway\TableGateway;

class PlayedWhiteCardTable
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

    public function getPlayedWhiteCard($id)
    {
        $id  = (int) $id;
        $rowset = $this->tableGateway->select(array('id' => $id));
        $row = $rowset->current();
        if (!$row) {
            throw new \Exception("Could not find row $id");
        }
        return $row;
    }
    
    public function savePlayedWhiteCard(PlayedWhiteCard $playedWhiteCard)
    {
        $data = array(
            'turn_id'       => $playedWhiteCard->turn_id,
            'user_id'       => $playedWhiteCard->user_id,
            'white_card_id' => $playedWhiteCard->white_card_id,
            'won'           => $playedWhiteCard->won,       
        );

        $id = (int)$playedWhiteCard->id;
        if ($id == 0) {
            $this->tableGateway->insert($data);
            return $this->tableGateway->lastInsertValue;
        } else {
            if ($this->getPlayedWhiteCard($id)) {
                $this->tableGateway->update($data, array('id' => $id));
                return $id;
            } else {
                throw new \Exception('Form id does not exist');
            }
        }
    }

    public function deletePlayedWhiteCard($id)
    {
        $this->tableGateway->delete(array('id' => $id));
    }
    
    public function getCardsOfTurn($turn_id) {
        $result = $this->tableGateway->select(array('turn_id' => $turn_id));
        $data = array();
        foreach($result as $playedWhiteCard)
          $data[] = $playedWhiteCard->toArray();
        return $data;
    }
}