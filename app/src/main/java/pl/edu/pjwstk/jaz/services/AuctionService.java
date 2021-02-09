package pl.edu.pjwstk.jaz.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.AuctionView;
import pl.edu.pjwstk.jaz.entities.*;
import pl.edu.pjwstk.jaz.exceptions.*;
import pl.edu.pjwstk.jaz.requests.AuctionRequest;
import pl.edu.pjwstk.jaz.requests.ParameterRequest;
import pl.edu.pjwstk.jaz.requests.PhotoRequest;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AuctionService {
    private final EntityManager em;
    private final CategoryService categoryService;

    public AuctionService(EntityManager em, CategoryService categoryService) {
        this.em = em;
        this.categoryService = categoryService;
    }

    public AuctionView addAuction(AuctionRequest auctionRequest){
        if (categoryService.findCategoryById(auctionRequest.getCategory_id()).isEmpty())
            throw new BadRequestException();
        var auction = new Auction();
        auction.setTitle(auctionRequest.getTitle());
        auction.setDescription(auctionRequest.getDescription());
        auction.setPrice(auctionRequest.getPrice());
        auction.setVersion(1);
        auction.setCategory(categoryService.findCategoryById(auctionRequest.getCategory_id()).get());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auction.setUserEntity((UserEntity) auth.getPrincipal());

        auction.setPhotos(getAuctionPhotos(auctionRequest.getPhotos(), auction));

        auction.setParameters(addAuctionParameters(auctionRequest.getParameters(),auction));

        em.persist(auction);

        return showAuction(auction.getId());
    }

    public AuctionView editAuction(int auctionId, AuctionRequest auctionRequest, int version){
        if (findAuctionById(auctionId).isEmpty())
            throw new EntityNotFoundException();
        var auction = findAuctionById(auctionId).get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (UserEntity) auth.getPrincipal();

        if (!auction.getUserEntity().getUsername().equals(user.getUsername()))
            throw new ForbiddenException();

        if (categoryService.findCategoryById(auctionRequest.getCategory_id()).isEmpty())
            throw new BadRequestException();

        if (auction.getVersion() != version)
            throw new ConflictException();

        auction.setTitle(auctionRequest.getTitle());
        auction.setDescription(auctionRequest.getDescription());
        auction.setPrice(auctionRequest.getPrice());
        auction.setVersion(auction.getVersion()+1);

        auction.setCategory(categoryService.findCategoryById(auctionRequest.getCategory_id()).get());

        auction.setPhotos(getAuctionPhotos(auctionRequest.getPhotos(), auction));

        auction.setParameters(editAuctionParameters(auctionRequest.getParameters(),auction));

        em.merge(auction);

        return showAuction(auctionId);
    }

    public AuctionView showAuction(int auctionId){
        if (findAuctionById(auctionId).isEmpty())
            throw new EntityNotFoundException();
        Auction auction = findAuctionById(auctionId).get();
        return new AuctionView(auction);
    }

    public List<String> listAllAuctions(){
        if (getAllAuctions().isEmpty())
            return null;
        List<String> stringList = new LinkedList<>();
        List<Auction> auctionList = getAllAuctions().get();
        for (Auction auction : auctionList){
            AuctionView auctionView = new AuctionView(auction);
            stringList.add(auctionView.toStringShortVersion());
        }
        return stringList;
    }

    public Optional<Auction> findAuctionById(int id){
        try {
            return Optional.ofNullable(em.createQuery("SELECT a FROM Auction a WHERE a.id = :id", Auction.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Auction>> getAllAuctions(){
        try {
            return Optional.ofNullable(em.createQuery("SELECT a FROM Auction a WHERE a.id > 0", Auction.class)
                    .getResultList());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<Parameter> getParameterByKey(String key){
        try {
            return Optional.ofNullable(em.createQuery("SELECT p FROM Parameter p WHERE p.key = :key", Parameter.class)
                    .setParameter("key", key)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    public Optional<AuctionParameter> getAuctionParameterByParameterAndAuction(Parameter parameter, Auction auction){
        try {
            return Optional.ofNullable(em.createQuery("SELECT ap FROM AuctionParameter ap " +
                    "WHERE ap.parameter = :parameter AND ap.auction = :auction", AuctionParameter.class)
                    .setParameter("parameter", parameter)
                    .setParameter("auction", auction)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        }
    }

    private List<Photo> getAuctionPhotos(List<PhotoRequest> photos, Auction auction){
        List<Photo> list = new LinkedList<>();
        for (PhotoRequest photoRequest : photos) {
            Photo photo = new Photo();

            photo.setAuction(auction);
            photo.setName(photoRequest.getName());
            photo.setPosition(photoRequest.getPosition());
            list.add(photo);
        }
        return list;
    }

    private List<AuctionParameter> addAuctionParameters(List<ParameterRequest> parameters, Auction auction){
        List<AuctionParameter> list = new LinkedList<>();
        for (ParameterRequest parameterRequest : parameters){
            AuctionParameter auctionParameter;
            Parameter parameter;
            if (getParameterByKey(parameterRequest.getKey()).isPresent()){
                parameter = getParameterByKey(parameterRequest.getKey()).get();
                auctionParameter = new AuctionParameter();
                auctionParameter.setParameter(parameter);
                auctionParameter.setValue(parameterRequest.getValue());
                auctionParameter.setAuction(auction);

            } else {
                auctionParameter = new AuctionParameter();
                parameter = new Parameter();
                parameter.setKey(parameterRequest.getKey());
                auctionParameter.setParameter(parameter);
                auctionParameter.setValue(parameterRequest.getValue());
                auctionParameter.setAuction(auction);
            }
            list.add(auctionParameter);
        }
        return list;
    }

    private List<AuctionParameter> editAuctionParameters(List<ParameterRequest> parameters, Auction auction){
        List<AuctionParameter> list = new LinkedList<>();
        for (ParameterRequest parameterRequest : parameters){
            AuctionParameter auctionParameter;
            Parameter parameter;
            if (getParameterByKey(parameterRequest.getKey()).isPresent()){
                parameter = getParameterByKey(parameterRequest.getKey()).get();
                if (getAuctionParameterByParameterAndAuction(parameter, auction).isPresent()){
                    auctionParameter = getAuctionParameterByParameterAndAuction(parameter,auction).get();
                    auctionParameter.setValue(parameterRequest.getValue());
                } else {
                    auctionParameter = new AuctionParameter();
                    auctionParameter.setParameter(parameter);
                    auctionParameter.setValue(parameterRequest.getValue());
                    auctionParameter.setAuction(auction);
                }
            } else {
                auctionParameter = new AuctionParameter();
                parameter = new Parameter();
                parameter.setKey(parameterRequest.getKey());
                auctionParameter.setParameter(parameter);
                auctionParameter.setValue(parameterRequest.getValue());
                auctionParameter.setAuction(auction);
            }
            list.add(auctionParameter);
        }
        return list;
    }

}
