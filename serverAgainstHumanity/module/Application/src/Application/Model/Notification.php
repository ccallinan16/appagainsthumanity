<?php
namespace Application\Model;

class Notification
{
    const notification_new_game      = 0;  //important notification
    const notification_new_round     = 1;  //important notification
    const notification_choose_black  = 2;  //important notification
    const notification_chosen_black  = 3;
    const notification_choose_white  = 4;  //important notification
    const notification_chosen_white  = 5;
    const notification_choose_winner = 6;  //important notification
    const notification_chosen_winner = 7;  //important notification

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