package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import controller.AdminController;
import controller.DroughtForecastController;
import controller.HelloController;
import controller.LoginController;
import controller.LogoutController;
import controller.MainController;
import controller.NoticeController;
import controller.RegisterController;
import controller.WaterForecastController;
import controller.WaterRequestController;
import controller.WaterViewController;
import dao.DroughtForecastAttachedFileDAO;
import dao.NoticeAttachedFileDAO;
import dao.WaterForecastAttachedFileDAO;
import dao.WaterRequestAttachedFileDAO;
import dao.WaterViewAttachedFileDAO;
import hacheon.HacheonDetailListService;
import hacheon.HacheonListService;
import service.DroughtForecastService;
import service.NoticeService;
import service.WaterForecastService;
import service.WaterRequestService;
import service.WaterViewService;
import spring.AuthService;
import waterGate.serice.WaterGateDataSerice;
import waterGate.serice.WaterGateSerice;

@Configuration
public class ControllerConfig {

	@Autowired
	private AuthService authService;
	
	@Autowired
    private WaterForecastService waterForecastService;

    @Autowired
    private WaterForecastAttachedFileDAO waterForecastAttachedFileDAO;
    
    @Autowired
    private WaterViewService waterViewService;

    @Autowired
    private WaterViewAttachedFileDAO waterViewAttachedFileDAO;
    
    @Autowired
    private DroughtForecastService droughtForecastService;

    @Autowired
    private DroughtForecastAttachedFileDAO droughtForecastAttachedFileDAO;
    
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeAttachedFileDAO noticeAttachedFileDAO;
	
    @Autowired
    private WaterRequestService waterRequestService;
    
    @Autowired
    private WaterRequestAttachedFileDAO waterRequestAttachedFileDAO;
    
    
	@Bean
	public RegisterController registerController() {
		return new RegisterController();
	}
	
	@Bean
	public HelloController helloController() {
		return new HelloController();
	}

	@Bean
	public LoginController loginController() {
		LoginController controller = new LoginController();
		controller.setAuthService(authService);
		return new LoginController();
	}

	@Bean
	public LogoutController logoutController() {
		return new LogoutController();
	}
	
	@Bean
	public AdminController adminController() {
		return new AdminController();
	}
	
	@Bean
	public MainController mainController() {
		return new MainController();
	}
	
	@Bean
    public NoticeController noticeController() {
        return new NoticeController(noticeService, noticeAttachedFileDAO);
    }
	

    @Bean
    public WaterRequestController waterRequestController() {
    	return new WaterRequestController(waterRequestService, waterRequestAttachedFileDAO);
    }

    @Bean
    public WaterForecastController waterForecastController() {
        return new WaterForecastController(waterForecastService, waterForecastAttachedFileDAO );
    }
    
    @Bean
    public WaterViewController waterViewController() {
        return new WaterViewController(waterViewService, waterViewAttachedFileDAO);
    }

    @Bean
    public DroughtForecastController droughtForecastController() {
        return new DroughtForecastController(droughtForecastService, droughtForecastAttachedFileDAO);
    }
    
    @Bean
    public WaterGateDataSerice waterGateDataSerice() {
    	return new WaterGateDataSerice();
    }
	
    @Bean
    public WaterGateSerice waterGateSerice() {
    	return new WaterGateSerice();
    }
    
    @Bean
    public HacheonListService hacheonListService() {
    	return new HacheonListService();
    }
    
    @Bean
    public HacheonDetailListService hacheonDetailListService() {
    	return new HacheonDetailListService();
    }
    
}
