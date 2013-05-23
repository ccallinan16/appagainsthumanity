<?php
namespace Application\Model;

use Zend\Db\TableGateway\TableGateway;

class DealtWhiteCardTable
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

    public function getDealtWhiteCard($id)
    {
        $id  = (int) $id;
        $rowset = $this->tableGateway->select(array('id' => $id));
        $row = $rowset->current();
        if (!$row) {
            throw new \Exception("Could not find row $id");
        }
        return $row;
    }
    
    public function saveDealtWhiteCard(DealtWhiteCard $dealtWhiteCard)
    {
        $data = array(
            'game_id'       => $dealtWhiteCard->game_id,
            'white_card_id' => $dealtWhiteCard->white_card_id,
            'user_id'       => $dealtWhiteCard->user_id,
            
        );

        $id = (int)$dealtWhiteCard->id;
        if ($id == 0) {
            $this->tableGateway->insert($data);
            return $this->tableGateway->lastInsertValue;
        } else {
            if ($this->getDealtWhiteCard($id)) {
                $this->tableGateway->update($data, array('id' => $id));
                return $id;
            } else {
                throw new \Exception('Form id does not exist');
            }
        }
    }

    public function deleteDealtWhiteCard($id)
    {
        $this->tableGateway->delete(array('id' => $id));
    }
    
    public function dropDealtCards($gameId, $userId) {
        $this->tableGateway->delete(array('game_id' => $gameId,
                                          'user_id' => $userId));
    }
    
    public function removeDealtWhiteCard($game_id, $white_card_id, $user_id) {
        $this->tableGateway->delete(array('game_id'       => $game_id,
                                          'white_card_id' => $white_card_id,
                                          'user_id'       => $user_id));
    }    
    
    public function dealCards($gameId, $userId, $cards) {
        $card = new DealtWhiteCard();
        $card->setId(0);
        $card->setGameId($gameId);
        $card->setUserId($userId);
        foreach($cards as $card_id) {
            $card->setWhiteCardId($card_id);
            $this->saveDealtWhiteCard($card);
        }
    }
    
    public function getDealtWhiteCardsOfGame($game_id) {
        $result = $this->tableGateway->select(array('game_id' => $game_id));
        $data = array();
        foreach($result as $dealtWhiteCard)
          $data[] = $dealtWhiteCard->toArray();
        return $data;
    }
    
    public function getDealtWhiteCardsOfUser($user_id, $game_id) {
        $result = $this->tableGateway->select(array('user_id' => $user_id,
                                                    'game_id' => $game_id));
        $data = array();
        foreach($result as $dealtWhiteCard)
          $data[] = $dealtWhiteCard->toArray();
        return $data;
    }
    
    public function isDealtWhiteCard($game_id, $white_card_id, $user_id) {
        $game_id  = (int) $game_id;
        $white_card_id  = (int) $white_card_id;
        $user_id  = (int) $user_id;
        $rowset = $this->tableGateway->select(array('game_id' => $game_id,
                                                    'white_card_id' => $white_card_id,
                                                    'user_id' => $user_id ));
        $row = $rowset->current();
        if (!$row) {
            return false;
        }
        return true;
    }
}