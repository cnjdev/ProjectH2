package com.example.demo.controller;
 
import java.util.List;
import java.util.Optional;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
 
import com.example.demo.model.Session;
import com.example.demo.repo.SessionRepository; 
 
@RestController
@RequestMapping("/api")
public class RestApiController {
 
    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);
  
    @Autowired
    private SessionRepository sessionRepo;
    
    public ResponseEntity responseError(String error){
        logger.error(error);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }
    
    // -------------------Retrieve All Sessions ---------------------------------------------
    @RequestMapping(value = "/session/all", method = RequestMethod.GET)
    public ResponseEntity<List<Session>> listAllSessions() {
        logger.info("Fetching all Sessions");
        
        // This returns a JSON or XML with the users
		List<Session> sessions = sessionRepo.findAll();
        if (sessions.isEmpty()) {
            return responseError("No sessions found.");
        }
        
        return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
    }
 
    // -------------------Retrieve Single Session ------------------------------------------
    @RequestMapping(value = "/session/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSession(@PathVariable("id") long id) {
        logger.info("Fetching Session with id {}", id);
        
        Optional<Session> opt = sessionRepo.findById(id);
        if (!opt.isPresent()) {
            return responseError("Session with id " + id + " not found.");
        }
        
        Session session = opt.get();
        return new ResponseEntity<Session>(session, HttpStatus.OK);
    }
    
    // ----------------- Add New Session with GET request ----------------------------------
    @RequestMapping(path="/session/add", method = RequestMethod.GET) 
	public ResponseEntity<?> addNewSession(@RequestParam String name, UriComponentsBuilder ucBuilder) {
		// @RequestParam means it is a parameter from the GET or POST request
        logger.info("Creating Session");

        Session s = new Session(name);
		sessionRepo.save(s);
		
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/session/{id}").buildAndExpand(s.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
    // -------------------Create a Session -------------------------------------------
    @RequestMapping(value = "/session/new", method = RequestMethod.POST)
    public ResponseEntity<?> createSession(@RequestBody Session session, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Session : {}", session);
 
        session.refresh();
        sessionRepo.save(session);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/session/{id}").buildAndExpand(session.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
 
    // ------------------- Update a Session ------------------------------------------------
    @RequestMapping(value = "/session/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSession(@PathVariable("id") long id, @RequestBody Session session) {
        logger.info("Updating Session with id {}", id);
 
        Optional<Session> opt = sessionRepo.findById(id);
 
        if (!opt.isPresent()) {
            return responseError("Unable to update. Session with id " + id + " not found.");
        }
 
        Session currSession = opt.get();
        currSession.setName(session.getName());
        currSession.refresh();
        
        sessionRepo.save(currSession);
        return new ResponseEntity<Session>(currSession, HttpStatus.OK);
    }

    // ------------------- Delete a Session -----------------------------------------
    @RequestMapping(value = "/session/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSession(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Session with id {}", id);
 
        Optional<Session> opt = sessionRepo.findById(id);
        if (!opt.isPresent()) {
            return responseError("Unable to delete. Session with id " + id + " not found.");
        }
        
        sessionRepo.deleteById(id);
        return new ResponseEntity<Session>(HttpStatus.NO_CONTENT);
    }
    
    // ------------------- Delete All Sessions -----------------------------
    @RequestMapping(value = "/session/all", method = RequestMethod.DELETE)
    public ResponseEntity<Session> deleteAllSessions() {
        logger.info("Deleting All Sessions");
 
        sessionRepo.deleteAll();
        return new ResponseEntity<Session>(HttpStatus.NO_CONTENT);
    }

}