<?php
namespace Application\Model\Rulebook;

abstract class Rulebook {

  //constants independant of gametype
  const BLACK_MAX_ID = 75;
  const WHITE_MAX_ID = 459;

  //rpc instance for table- and other callbacks
  protected $rpc;
  
  public function __construct($new_rpc) {
    $this->rpc = $new_rpc;
  }
  
  public static function createRulebook($rpc) {
    $rulebook = new DefaultRulebook($rpc);
    return $rulebook;
  }
  
  /*
   * TABLE GETTERS
   */   
  
  protected function getDealtBlackCardTable() {
    return $this->rpc->getDealtBlackCardTable();
  }
  
  protected function getDealtWhiteCardTable() {
    return $this->rpc->getDealtWhiteCardTable();
  }
  
  protected function getPlayedWhiteCardTable() {
    return $this->rpc->getPlayedWhiteCardTable();
  }
  
  protected function getGameTable() {
    return $this->rpc->getGameTable();
  }
  
  protected function getNotificationTable() {
    return $this->rpc->getNotificationTable();
  }
  
  protected function getParticipationTable() {
    return $this->rpc->getParticipationTable();
  }
  
  protected function getTurnTable() {
    return $this->rpc->getTurnTable();
  }
  
  protected function getUserTable() {
    return $this->rpc->getUserTable();
  }
  
  public abstract function validateGameData($data);
  public abstract function onCreateGame($gameId, $userId);
  public abstract function onBlackCardChosen($user_id, $turn_id, $card_id);
  public abstract function onWhiteCardChosen($user_id, $turn_id, $card_id);  
  
}

