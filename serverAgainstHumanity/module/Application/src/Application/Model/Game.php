<?php
namespace Application\Model;

class Game
{
    public $id;
    public $roundcap;
    public $scorecap;
    public $updated;
    
    public function exchangeArray($data)
    {
        $this->id        = (isset($data['id']))       ? $data['id']       : null;
        $this->roundcap  = (isset($data['roundcap'])) ? $data['roundcap'] : null;
        $this->scorecap  = (isset($data['scorecap'])) ? $data['scorecap'] : null;
        $this->updated   = (isset($data['updated']))  ? $data['updated']  : null;
    }
}