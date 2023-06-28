package concerttours.jobs;
import concerttours.model.TokenModel;
import concerttours.service.TokenModelService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class TokenCronJob extends AbstractJobPerformable<CronJobModel>
{
    private static final Logger LOG = Logger.getLogger(TokenCronJob.class);
    private TokenModelService tokenModelService;
    private ConfigurationService configurationService;

    @Required
    public TokenModelService getTokenModelService()
    {
        return tokenModelService;
    }

    @Required
    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

    @Required
    public void setTokenModelService(final TokenModelService tokenModelService)
    {
        this.tokenModelService = tokenModelService;
    }

    @Required
    public void setConfigurationService(final ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
    @Override
    public PerformResult perform(final CronJobModel cronJob)
    {
        LOG.info("Performing cronJob");
        TokenModel tokenModel = tokenModelService.getToken();
        tokenModelService.saveToken(tokenModel.getToken() + " 1");
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }
}