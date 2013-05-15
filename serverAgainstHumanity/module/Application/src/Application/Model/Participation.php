<?php
namespace Application\Model;

class Participation
{
    public $id;
    public $game_id;
    public $user_id;
    public $score;

    public function exchangeArray($data)
    {
        $this->id     = (isset($data['id'])) ? $data['id'] : null;
        $this->game_id  = (isset($data['game_id'])) ? $data['game_id'] : null;
        $this->user_id  = (isset($data['user_id'])) ? $data['user_id'] : null;
        $this->score  = (isset($data['score'])) ? $data['score'] : null;
    }
    
    public function setId($new_id) {
        $this->id = $new_id;
    }
    
    public function setGameId($new_game_id) {
        $this->game_id = $new_game_id;
    }
    
    public function setUserId($new_user_id) {
        $this->user_id = $new_user_id;
    }
    
    public function setScore($new_score) {
        $this->score = $new_score;
    }
}