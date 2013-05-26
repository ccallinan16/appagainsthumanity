<?php
namespace Application\Model;

class DealtBlackCard
{
    public $id;
    public $game_id;
    public $black_card_id;
    public $user_id;

    public function exchangeArray($data)
    {
        $this->id            = (isset($data['id']))            ? $data['id']            : null;
        $this->game_id       = (isset($data['game_id']))       ? $data['game_id']       : null;
        $this->black_card_id = (isset($data['black_card_id'])) ? $data['black_card_id'] : null;
        $this->user_id       = (isset($data['user_id']))       ? $data['user_id']       : null;      
    }
    
    public function toArray() {
        $data = array();
        $data['id']            = $this->id;
        $data['game_id']       = $this->game_id;
        $data['black_card_id'] = $this->black_card_id;
        $data['user_id']       = $this->user_id;        
        return $data;
    }
    
    public function setId($new_id) {
        $this->id = $new_id;
    }
    
    public function setGameId($new_game_id) {
        $this->game_id = $new_game_id;
    }
    
    public function setBlackCardId($new_black_card_id) {
        $this->black_card_id = $new_black_card_id;
    }
    
    public function setUserId($new_user_id) {
        $this->user_id = $new_user_id;
    }
}