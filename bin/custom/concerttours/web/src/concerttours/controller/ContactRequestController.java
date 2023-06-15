package concerttours.controller;

import concerttours.model.ContactRequestsModel;
import concerttours.service.ContactRequestService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactRequestController {
    @Autowired
    private ContactRequestService contactRequestService;
    @Autowired
    private ModelService modelService;

    @RequestMapping(value = "/contactRequest", method = RequestMethod.GET)
    public String handleRequest
            (final Model model, @RequestParam String sender) throws Exception {

        ContactRequestsModel contactRequest = null;
        if (sender != null && !sender.equals("")) {
            try {
                contactRequest = contactRequestService.getContactRequest(sender);
            } catch (final UnknownIdentifierException e) {
            }
        }

        model.addAttribute("contactRequest", contactRequest);
        return "contactRequest";
    }

    @RequestMapping(value = "/contactRequest", method = RequestMethod.POST)
    public String handleRequest
            (final Model model, @RequestParam String sender, @RequestParam String newSender, @RequestParam String newMessage) throws Exception {
        ContactRequestsModel contactRequest = null;
        if (sender != null && !sender.equals("")) {
            try {
                contactRequest = contactRequestService.getContactRequest(sender);
            } catch (final UnknownIdentifierException e) {
            }
        }
        if (contactRequest == null) {
            contactRequest = new ContactRequestsModel();
            modelService.attach(contactRequest);
        }

        if (newSender != null) {
            contactRequest.setSender(newSender);
        }
        if (newMessage != null) {
            contactRequest.setMessage(newMessage);
        }
        modelService.save(contactRequest);

        model.addAttribute("contactRequest", contactRequest);
        return "contactRequest";
    }
    public void setContactRequestService(final ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }
}
