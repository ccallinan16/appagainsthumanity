<?php
namespace Application\Model;

class User
{
    public $id;
    public $username;

    public function exchangeArray($data)
    {
        $this->id     = (isset($data['id'])) ? $data['id'] : null;
        $this->username  = (isset($data['username'])) ? $data['username'] : null;
    }
    
    public function toArray() {
        $data = array();
        $data['id']       = $this->id;
        $data['username'] = $this->username;
        return $data;
    }
    
    public function setId($new_id) {
        $this->id = $new_id;
    }

    public function setUsername($new_username) {
        $this->username = $new_username;
    }
    
    public function __toString()
    {
    	return "User: ".$this->id.", name: ".$this->username;
    }
}