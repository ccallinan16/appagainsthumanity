<?php
namespace Application\Model;

class PlayedWhiteCard
{
    public $id;
    public $turn_id;
    public $user_id;
    public $white_card_id;
    public $won;
    

    public function exchangeArray($data)
    {
        $this->id            = (isset($data['id']))            ? $data['id']            : null;
        $this->turn_id       = (isset($data['turn_id']))       ? $data['turn_id']       : null;
        $this->user_id       = (isset($data['user_id']))       ? $data['user_id']       : null;   
        $this->white_card_id = (isset($data['white_card_id'])) ? $data['white_card_id'] : null;
        $this->won           = (isset($data['won']))           ? $data['won']           : null;           
    }
    
    public function toArray() {
        $data = array();
        $data['id']            = $this->id;
        $data['turn_id']       = $this->turn_id;
        $data['user_id']       = $this->user_id;   
        $data['white_card_id'] = $this->white_card_id;
        $data['won']           = $this->won;     
        return $data;
    }
   
    public function setId($new_id) {
        $this->id = $new_id;
    }
    
    public function setTurnId($new_turn_id) {
        $this->turn_id = $new_turn_id;
    }
    
    public function setUserId($new_user_id) {
        $this->user_id = $new_user_id;
    }
    
    public function setWhiteCardId($new_white_card_id) {
        $this->white_card_id = $new_white_card_id;
    }
    
    public function setWon($new_won) {
        $this->won = $new_won;
    }    

}