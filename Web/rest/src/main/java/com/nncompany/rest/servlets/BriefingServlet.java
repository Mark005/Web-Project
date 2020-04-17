package com.nncompany.rest.servlets;

import com.nncompany.api.interfaces.services.IBriefingService;
import com.nncompany.api.model.entities.Briefing;
import com.nncompany.impl.util.UserKeeper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rest/creds")
public class BriefingServlet {

    @Autowired
    IBriefingService briefingService;

    //TODO get TOTAL count  https://www.baeldung.com/hibernate-pagination
    @ApiOperation(value = "Get briefings with pagination, count elements returned in header 'X-Total-Count' ")
    @GetMapping("/briefings")
    public ResponseEntity<List<Briefing>> getAllBriefings(@RequestParam Integer offset,
                                                          @RequestParam Integer limit){
        if(offset< 1 && limit < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Briefing> briefings = briefingService.getWithPagination(offset, limit);
        if(briefings != null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("X-Total-Count", briefings.size()+"");
            return ResponseEntity.ok().headers(responseHeaders).body(briefings);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Add new briefing")
    @PostMapping("/briefings")
    public ResponseEntity addBriefing(@RequestBody Briefing briefing){
        briefingService.save(briefing);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO check negative @PathVariable
    @ApiOperation(value = "Update briefing by id")
    @PutMapping("/briefings/{id}")
    public ResponseEntity updateBriefing(@PathVariable Integer id,
                                         @RequestBody Briefing briefing){
        if(briefingService.get(id) != null){
            briefing.setId(id);
            briefingService.update(briefing);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Deleter briefing by id")
    @DeleteMapping("/briefings/{id}")
    public ResponseEntity deleteBriefing(@PathVariable Integer id){
        Briefing briefing = briefingService.get(id);
        if(briefing != null){
            briefingService.delete(briefing);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
