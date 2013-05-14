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
}