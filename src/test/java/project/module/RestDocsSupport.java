package project.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project.mapjiri.domain.menu.controller.MenuController;
import project.mapjiri.domain.menu.service.MenuService;
import project.mapjiri.domain.menustar.controller.MenuStarController;
import project.mapjiri.domain.menustar.service.MenuStarService;
import project.mapjiri.domain.place.controller.LocationController;
import project.mapjiri.domain.place.service.PlaceService;
import project.mapjiri.domain.placeStar.controller.PlaceStarController;
import project.mapjiri.domain.placeStar.service.PlaceStarService;
import project.mapjiri.domain.restaurant.controller.RestaurantController;
import project.mapjiri.domain.restaurant.controller.SearchController;
import project.mapjiri.domain.restaurant.service.RestaurantService;
import project.mapjiri.domain.restaurant.service.SearchService;
import project.mapjiri.domain.review.controller.ReviewController;
import project.mapjiri.domain.review.service.ReviewService;
import project.mapjiri.domain.user.controller.UserController;
import project.mapjiri.domain.user.provider.JwtTokenProvider;
import project.mapjiri.domain.user.service.MailService;
import project.mapjiri.domain.user.service.UserService;
import project.mapjiri.global.client.KakaoMapClient;
import project.mapjiri.global.config.WebSecurityConfig;
import project.mapjiri.support.config.RestDocsConfig;

@WebMvcTest(controllers = {
        SearchController.class,
        UserController.class,
        PlaceStarController.class,
        MenuStarController.class,
        MenuController.class,
        LocationController.class,
        ReviewController.class,
        RestaurantController.class
})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({WebSecurityConfig.class, RestDocsConfig.class})
public abstract class RestDocsSupport {

    @Autowired
    protected RestDocumentationResultHandler restDocsHandler;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    protected KakaoMapClient kakaoMapClient;

    @MockitoBean
    protected SearchService searchService;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected MailService mailService;

    @MockitoBean
    protected PlaceStarService placestarService;

    @MockitoBean
    protected MenuStarService menustarService;

    @MockitoBean
    protected MenuService menuService;

    @MockitoBean
    protected PlaceService placeService;

    @MockitoBean
    protected ReviewService reviewService;

    @MockitoBean
    protected RestaurantService restaurantService;
}
