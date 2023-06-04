package de.hybris.platform.b2bacceleratoraddon.component.renderer;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.b2bacceleratorservices.model.actions.ReorderActionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class B2BAcceleratorReorderActionRendererTest
{
	@InjectMocks
	private B2BAcceleratorReorderActionRenderer<ReorderActionModel> renderer;

	@Mock
	private CMSComponentService cmsComponentService;

	@Mock
	private UiExperienceService uiExperienceService;

	private String unknownUiExperiencePrefix="responsive/common/js";

	@Mock
	private ReorderActionModel model;

	@Before
	public void init() {
		Mockito.when(cmsComponentService.getReadableEditorProperties(any(ReorderActionModel.class))).thenReturn(new ArrayList<>());
		Mockito.when(uiExperienceService.getUiExperienceLevel()).thenReturn(null);
		ReflectionTestUtils.setField(renderer, "unknownUiExperiencePrefix", unknownUiExperiencePrefix);
	   Mockito.when(model.getItemtype()).thenReturn("ReorderAction");
	}

	@Test
	public void test_renderComponent_should_success() throws IOException, ServletException{
		PageContext pageContext = Mockito.mock(PageContext.class);

		renderer.renderComponent(pageContext, model);

		Mockito.verify(pageContext, Mockito.times(1)).include("/WEB-INF/views/addons/b2bacceleratoraddon/responsive/common/js/cms/reorderaction.jsp");
	}

}
