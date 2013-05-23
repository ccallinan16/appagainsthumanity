<?php
namespace Application\Model;

class User
{
    public $id;
    public $gcm_id;
    public $username;

    
    public function exchangeArray($data)
    {
        $this->id     = (isset($data['id'])) ? $data['id'] : null;
        $this->username  = (isset($data['username'])) ? $data['username'] : null;
        $this->gcm_id  = (isset($data['gcm_id'])) ? $data['gcm_id'] : null;
    }
    
    public function toArray() {
        $data = array();
        if ($this->id != null && $this->id > 0)
        	$data['id']       = $this->id;
        
        if ($this->username != null)
       		$data['username'] = $this->username;
        	        
        if ($this->gcm_id != null)
        	$data['gcm_id']       = $this->gcm_id;
        
        return $data;
    }
    
    public function setId($new_id) {
        $this->id = $new_id;
    }

    public function setUsername($new_username) {
        $this->username = $new_username;
    }
    
    public function setGCMID($gcm_id) {
    	$this->gcm_id = $gcm_id;
    }
    
    public function __toString()
    {
    	$string = "[User: ";
    	
    	if ($this->id != null)
    		$string .= " id: ".$this->id;
    	
    	if ($this->username != null)
    		$string .= " name: ".$this->username;
    		    		
    	if ($this->gcm_id != null)
    		$string .= " gcm_id: ".$this->gcm_id;
    	
    	$string .= "]\n";
    		    	 
    	return $string;
    }
}