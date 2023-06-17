package concerttours.service.impl;

import concerttours.daos.TokenDAO;
import concerttours.model.TokenModel;
import concerttours.service.TokenModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTokenModelService implements TokenModelService
{
    private TokenDAO tokenDAO;

    private ModelService modelService;

    @Required
    public void setTokenDAO(final TokenDAO tokenDAO)
    {
        this.tokenDAO = tokenDAO;
    }

    @Required
    public void setModelService(final ModelService modelService) {this.modelService = modelService; }
    @Override
    public TokenModel getToken() {
        return tokenDAO.findToken();
    }

    @Override
    public void saveToken(String token) {
        TokenModel tokenModel = getToken();
        tokenModel.setToken(token);
        modelService.save(tokenModel);
    }

}
