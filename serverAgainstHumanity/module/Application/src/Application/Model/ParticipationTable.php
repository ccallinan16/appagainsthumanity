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
    
    /*
    * get participants of game in compressed form
    */    
    public function getParticipants($gameId) {
      //get table entries with game id
      $result = $this->tableGateway->select(array('game_id' => $gameId));  
      //initialize empty participants array
      $participants = array();            
      //add participant ids to array
      foreach ($result as $column)
        $participants[] = $column->user_id;
      return $participants;
    }
    
    /*
     * get complete data-set of participations of a given game-id
     */     
    public function getParticipationOfGame($game_id) {
      //get table entries for game
      $result = $this->tableGateway->select(array('game_id' => $game_id));  
      //prepare data
      $data = array();
      //append data
      foreach($result as $participation)
        $data[] = $participation->toArray();
        
      return $data;
    }
}