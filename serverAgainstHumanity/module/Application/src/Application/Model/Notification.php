<?php
namespace Application\Model;

class Notification
{
    const notification_new_game       = 0;
    const notification_new_round      = 1;
    const notification_new_round_czar = 2;
    const notification_chosen_black   = 3;
    const notification_chosen_white   = 4;
    const notification_chosen_winner  = 5;
    public $id;
    public $user_id;
    public $type;
    public $content_id;
    
    public function exchangeArray($data)
    {
        $this->id         = (isset($data['id']))         ? $data['id']         : null;
        $this->user_id    = (isset($data['user_id']))    ? $data['user_id']    : null;
        $this->type       = (isset($data['type']))       ? $data['type']       : null;
        $this->content_id = (isset($data['content_id'])) ? $data['content_id'] : null;
    }
    
    public function setId($new_id) {
        $this->id = $new_id;
    }
    
    public function setUserId($new_user_id) {
        $this->user_id = $new_user_id;
    }
    
    public function setType($new_type) {
        $this->type = $new_type;
    }
    
    public function setContentId($new_content_id) {
        $this->content_id = $new_content_id;
    }
    
    
}