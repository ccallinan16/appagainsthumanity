<?php
namespace Application\Model;

class User
{
    public $id;
    public $username;

    public function setId($new_id) {
        $this->id = $new_id;
    }

    public function setUsername($name) {
        $this->username = $name;
    }

    public function exchangeArray($data)
    {
        $this->id     = (isset($data['id'])) ? $data['id'] : null;
        $this->username  = (isset($data['username'])) ? $data['username'] : null;
    }
}