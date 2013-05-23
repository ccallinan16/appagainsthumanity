<?php
namespace Application\Model;

use Zend\Db\TableGateway\TableGateway;

class DealtBlackCardTable
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

    public function getDealtBlackCard($id)
    {
        $id  = (int) $id;
        $rowset = $this->tableGateway->select(array('id' => $id));
        $row = $rowset->current();
        if (!$row) {
            throw new \Exception("Could not find row $id");
        }
        return $row;
    }
    
    public function saveDealtBlackCard(DealtBlackCard $dealtBlackCard)
    {
        $data = array(
            'game_id'       => $dealtBlackCard->game_id,
            'black_card_id' => $dealtBlackCard->black_card_id,
            'user_id'       => $dealtBlackCard->user_id,
            
        );

        $id = (int)$dealtBlackCard->id;
        if ($id == 0) {
            $this->tableGateway->insert($data);
            return $this->tableGateway->lastInsertValue;
        } else {
            if ($this->getDealtBlackCard($id)) {
                $this->tableGateway->update($data, array('id' => $id));
                return $id;
            } else {
                throw new \Exception('Form id does not exist');
            }
        }
    }

    public function deleteDealtBlackCard($id)
    {
        $this->tableGateway->delete(array('id' => $id));
    }
    
    public function dropDealtCards($gameId, $userId) {
        $this->tableGateway->delete(array('game_id' => $gameId,
                                          'user_id' => $userId));
    }
    
    public function dealCards($gameId, $czarId, $cards) {
        $card = new DealtBlackCard();
        $card->setId(0);
        $card->setGameId($gameId);
        $card->setUserId($czarId);
        foreach($cards as $card_id) {
            $card->setBlackCardId($card_id);
            $this->saveDealtBlackCard($card);
        }
    }
    
    public function getDealtBlackCards($user_id, $game_id) {
        $result = $this->tableGateway->select(array('user_id' => $user_id,
                                                    'game_id' => $game_id));
        $data = array();
        foreach($result as $dealtBlackCard)
          $data[] = $dealtBlackCard->toArray();
        return $data;
    }
    
    public function isDealtBlackCard($game_id, $black_card_id, $user_id) {
        $game_id  = (int) $game_id;
        $black_card_id  = (int) $black_card_id;
        $user_id  = (int) $user_id;
        $rowset = $this->tableGateway->select(array('game_id' => $game_id,
                                                    'black_card_id' => $black_card_id,
                                                    'user_id' => $user_id ));
        $row = $rowset->current();
        if (!$row) {
            return false;
        }
        return true;
    }
}