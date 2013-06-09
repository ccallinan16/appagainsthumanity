<?php
namespace Application\Model;

class Game
{
    public $id;
    public $roundcap;
    public $scorecap;
    public $updated;
    public $winner;
    
    public function exchangeArray($data)
    {
        $this->id        = (isset($data['id']))       ? $data['id']       : null;
        $this->roundcap  = (isset($data['roundcap'])) ? $data['roundcap'] : null;
        $this->scorecap  = (isset($data['scorecap'])) ? $data['scorecap'] : null;
        $this->updated   = (isset($data['updated']))  ? $data['updated']  : null;
        $this->winner    = (isset($data['winner']))   ? $data['winner']   : null;
    }
    
    public function toArray() {
        $data = array();
        $data['id']       = $this->id;
        $data['roundcap'] = $this->roundcap;
        $data['scorecap'] = $this->scorecap;
        $data['updated']  = $this->updated;
        $data['winner']   = $this->winner;
        return $data;
    }
    
    public function setId($new_id) {
        $this->id = $new_id;
    }
    
    public function setRoundcap($new_roundcap) {
        $this->roundcap = $new_roundcap;
    }
    
    public function setScorecap($new_scorecap) {
        $this->scorecap = $new_scorecap;
    }
    
    public function setWinner($new_winner) {
        $this->winner = $new_winner;
    }
}