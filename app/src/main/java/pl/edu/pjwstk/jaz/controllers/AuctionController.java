package pl.edu.pjwstk.jaz.controllers;

import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.AuctionView;
import pl.edu.pjwstk.jaz.requests.AuctionRequest;
import pl.edu.pjwstk.jaz.services.AuctionService;

import java.util.List;

@RestController
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/addAuction")
    public AuctionView addAuction(@RequestBody AuctionRequest auctionRequest){
        return auctionService.addAuction(auctionRequest);
    }

    @PutMapping("/editAuction/{auctionId}/{version}")
    public AuctionView editAuction(@PathVariable int auctionId, @RequestBody AuctionRequest auctionRequest, @PathVariable int version){
        return auctionService.editAuction(auctionId, auctionRequest, version);
    }

    @GetMapping("/showAuction/{auctionId}")
    public AuctionView showAuction(@PathVariable int auctionId){
        return auctionService.showAuction(auctionId);
    }

    @GetMapping("/auctionList")
    public List<String> auctionList(){
       return auctionService.listAllAuctions();
    }
}
