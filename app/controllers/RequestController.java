package controllers;

import models.ConnectionRequest;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by lubuntu on 10/23/16.
 */
public class RequestController extends Controller {
    public Result sendRequest(Long senderid,Long receiverid) {
        if (senderid == null || receiverid == null || User.find.byId(senderid) == null || User.find.byId(receiverid) == null) {
            return ok();
        } else {
            ConnectionRequest c = new ConnectionRequest();
            c.sender = User.find.byId(senderid);
            c.receiver = User.find.byId(receiverid);
            c.status = ConnectionRequest.Status.WAITING;
            ConnectionRequest.db().save(c);
            return ok();
        }
    }

    public Result acceptRequest(Long requestid){
        if (requestid == null || User.find.byId(requestid) == null) {
            return ok();
        } else {
            ConnectionRequest c = ConnectionRequest.find.byId(requestid);
            c.sender.connections.add(c.receiver);
            c.receiver.connections.add(c.sender);
            c.status = ConnectionRequest.Status.ACCEPTED;
            ConnectionRequest.db().update(c);
            c.sender.update();
            c.receiver.update();
            User.db().update(c.sender);
            User.db().update(c.receiver);
            return ok();
        }
    }
}
