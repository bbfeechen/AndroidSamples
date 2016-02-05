package com.sony.imaging.app.srctrl;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.caution.CautionFragment;
import com.sony.imaging.app.base.caution.CautionLayout;
import com.sony.imaging.app.base.common.ExitScreenState;
import com.sony.imaging.app.base.common.ForceExitScreenState;
import com.sony.imaging.app.base.common.layout.ExitScreenLayout;
import com.sony.imaging.app.base.common.layout.ForceExitScreenLayout;
import com.sony.imaging.app.base.menu.layout.AFFlexiblePositionMenuLayout;
import com.sony.imaging.app.base.menu.layout.AFLocalPositionMenuLayout;
import com.sony.imaging.app.base.menu.layout.CreativeStyleMenuLayout;
import com.sony.imaging.app.base.menu.layout.DigitalZoomSettingLayout;
import com.sony.imaging.app.base.menu.layout.ExposureCompensationMenuLayout;
import com.sony.imaging.app.base.menu.layout.ExposureModeMenuLayout;
import com.sony.imaging.app.base.menu.layout.FlashCompensationMenuLayout;
import com.sony.imaging.app.base.menu.layout.Fn15LayerCreativeStyleLayout;
import com.sony.imaging.app.base.menu.layout.Fn15LayerExposureCompensationLayout;
import com.sony.imaging.app.base.menu.layout.Fn15LayerFlashCompensationLayout;
import com.sony.imaging.app.base.menu.layout.Fn15LayerMenuLayout;
import com.sony.imaging.app.base.menu.layout.Fn15LayerWhiteBalanceLayout;
import com.sony.imaging.app.base.menu.layout.FnMenuLayout;
import com.sony.imaging.app.base.menu.layout.FocusModeSpecialScreenMenuLayout;
import com.sony.imaging.app.base.menu.layout.ISOFn15LayerMenuLayout;
import com.sony.imaging.app.base.menu.layout.ISOSpecialScreenMenuLayout;
import com.sony.imaging.app.base.menu.layout.MovieModeMenuLayout;
import com.sony.imaging.app.base.menu.layout.PageMenuLayout;
import com.sony.imaging.app.base.menu.layout.SceneSelectionMenuLayout;
import com.sony.imaging.app.base.menu.layout.SetMenuLayout;
import com.sony.imaging.app.base.menu.layout.SpecialScreenMenuLayout;
import com.sony.imaging.app.base.menu.layout.SpecialScreenMenuLayoutHiddenEE;
import com.sony.imaging.app.base.menu.layout.UnknownItemMenuLayout;
import com.sony.imaging.app.base.menu.layout.WhiteBalanceAdjustmentMenuLayout;
import com.sony.imaging.app.base.menu.layout.WhiteBalanceMenuLayout;
import com.sony.imaging.app.base.shooting.AutoReviewForLimitedContShootingState;
import com.sony.imaging.app.base.shooting.AutoReviewState;
import com.sony.imaging.app.base.shooting.CustomWhiteBalanceEEState;
import com.sony.imaging.app.base.shooting.CustomWhiteBalanceExposureState;
import com.sony.imaging.app.base.shooting.FocusAdjustmentState;
import com.sony.imaging.app.base.shooting.MfAssistState;
import com.sony.imaging.app.base.shooting.MovieMenuState;
import com.sony.imaging.app.base.shooting.NormalCaptureState;
import com.sony.imaging.app.base.shooting.S1OffEEState;
import com.sony.imaging.app.base.shooting.S1OnEEState;
import com.sony.imaging.app.base.shooting.SelfTimerCaptureState;
import com.sony.imaging.app.base.shooting.ShootingState;
import com.sony.imaging.app.base.shooting.layout.AutoReviewForLimitedContShootingLayout;
import com.sony.imaging.app.base.shooting.layout.CustomWhiteBalanceConfLayout;
import com.sony.imaging.app.base.shooting.layout.CustomWhiteBalanceEELayout;
import com.sony.imaging.app.base.shooting.layout.FocusAdjustmentLayout;
import com.sony.imaging.app.base.shooting.layout.FocusLayout;
import com.sony.imaging.app.base.shooting.layout.GuideLayout;
import com.sony.imaging.app.base.shooting.layout.MfAssistLayout;
import com.sony.imaging.app.base.shooting.layout.ProgressLayout;
import com.sony.imaging.app.base.shooting.layout.S1OffLayout;
import com.sony.imaging.app.base.shooting.layout.ShootingLayout;
import com.sony.imaging.app.base.shooting.movie.MovieCaptureState;
import com.sony.imaging.app.base.shooting.movie.MovieRecStandbyState;
import com.sony.imaging.app.base.shooting.movie.MovieRecState;
import com.sony.imaging.app.base.shooting.movie.MovieSaveState;
import com.sony.imaging.app.base.shooting.movie.MovieStateBase;
import com.sony.imaging.app.base.shooting.movie.layout.MovieSaveLayout;
import com.sony.imaging.app.base.shooting.movie.trigger.MovieCaptureStateKeyHandler;
import com.sony.imaging.app.base.shooting.movie.trigger.MovieRecStandbyStateKeyHandler;
import com.sony.imaging.app.base.shooting.movie.trigger.MovieRecStateKeyHandler;
import com.sony.imaging.app.base.shooting.movie.trigger.MovieSaveStateKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.AutoReviewForLimitedContShootingStateKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.CustomWhiteBalanceCaptureKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.CustomWhiteBalanceEEKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.CustomWhiteBalanceExposureKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.EEStateKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.FocusAdjustmentKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.S1OnEEStateKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.SelfTimerCaptureStateKeyHandler;
import com.sony.imaging.app.base.shooting.trigger.ShootingStateKeyHandler;
import com.sony.imaging.app.fw.LayoutFactory;
import com.sony.imaging.app.fw.StateFactory;
import com.sony.imaging.app.srctrl.caution.CautionDisplayStateEx;
import com.sony.imaging.app.srctrl.menu.layout.LastBastionMenuLayoutEx;
import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.message.confirmation.NwConfirmationKeyHandler;
import com.sony.imaging.app.srctrl.network.message.confirmation.NwConfirmationLayout;
import com.sony.imaging.app.srctrl.network.message.confirmation.NwConfirmationState;
import com.sony.imaging.app.srctrl.network.message.fatal.NwFatalErrorKeyHandler;
import com.sony.imaging.app.srctrl.network.message.fatal.NwFatalErrorLayout;
import com.sony.imaging.app.srctrl.network.message.fatal.NwFatalErrorState;
import com.sony.imaging.app.srctrl.network.message.wpserror.NwWpsErrorKeyHandler;
import com.sony.imaging.app.srctrl.network.message.wpserror.NwWpsErrorState;
import com.sony.imaging.app.srctrl.network.message.wpserror.NwWpsInvalidLayout;
import com.sony.imaging.app.srctrl.network.message.wpserror.NwWpsTimeoutLayout;
import com.sony.imaging.app.srctrl.network.progress.connected.NwConnectedKeyHandler;
import com.sony.imaging.app.srctrl.network.progress.connected.NwConnectedLayout;
import com.sony.imaging.app.srctrl.network.progress.connected.NwConnectedState;
import com.sony.imaging.app.srctrl.network.progress.registering.NwWpsRegisteringKeyHandler;
import com.sony.imaging.app.srctrl.network.progress.registering.NwWpsRegisteringLayout;
import com.sony.imaging.app.srctrl.network.progress.registering.NwWpsRegisteringState;
import com.sony.imaging.app.srctrl.network.progress.restarting.RestartingKeyHandler;
import com.sony.imaging.app.srctrl.network.progress.restarting.RestartingLayout;
import com.sony.imaging.app.srctrl.network.progress.restarting.RestartingState;
import com.sony.imaging.app.srctrl.network.standby.standby.NwStandbyKeyHandler;
import com.sony.imaging.app.srctrl.network.standby.standby.NwStandbyLayout;
import com.sony.imaging.app.srctrl.network.standby.standby.NwStandbyState;
import com.sony.imaging.app.srctrl.network.waiting.waiting.NwWaitingKeyHandler;
import com.sony.imaging.app.srctrl.network.waiting.waiting.NwWaitingLayout;
import com.sony.imaging.app.srctrl.network.waiting.waiting.NwWaitingState;
import com.sony.imaging.app.srctrl.network.wpspbc.pbc.NwWpsPbcKeyHandler;
import com.sony.imaging.app.srctrl.network.wpspbc.pbc.NwWpsPbcLayout;
import com.sony.imaging.app.srctrl.network.wpspbc.pbc.NwWpsPbcState;
import com.sony.imaging.app.srctrl.network.wpspin.input.NwWpsPinInputKeyHandler;
import com.sony.imaging.app.srctrl.network.wpspin.input.NwWpsPinInputLayout;
import com.sony.imaging.app.srctrl.network.wpspin.input.NwWpsPinInputState;
import com.sony.imaging.app.srctrl.network.wpspin.print.NwWpsPinPrintKeyHandler;
import com.sony.imaging.app.srctrl.network.wpspin.print.NwWpsPinPrintLayout;
import com.sony.imaging.app.srctrl.network.wpspin.print.NwWpsPinPrintState;
import com.sony.imaging.app.srctrl.shooting.SRCtrlForceSettingState;
import com.sony.imaging.app.srctrl.shooting.keyhandler.AutoReviewStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.keyhandler.CaptureStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.keyhandler.DevelopmentStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.keyhandler.MfAssistKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.keyhandler.NormalCaptureStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.keyhandler.S1OffEEStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.keyhandler.S1OnEEStateKeyHandlerForTouchAF;
import com.sony.imaging.app.srctrl.shooting.keyhandler.S1OnEEStateKeyHandlerForTouchAFAssist;
import com.sony.imaging.app.srctrl.shooting.layout.AutoReviewLayoutEx;
import com.sony.imaging.app.srctrl.shooting.layout.SRCtrlStableLayout;
import com.sony.imaging.app.srctrl.shooting.layout.WifiIconLayout;
import com.sony.imaging.app.srctrl.shooting.state.CaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.CustomWhiteBalanceCaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.CustomWhiteBalanceControllerStateEx;
import com.sony.imaging.app.srctrl.shooting.state.CustomWhiteBalanceEEStateEx;
import com.sony.imaging.app.srctrl.shooting.state.CustomWhiteBalanceExposureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.DevelopmentStateEx;
import com.sony.imaging.app.srctrl.shooting.state.EEStateEx;
import com.sony.imaging.app.srctrl.shooting.state.ExposureModeCheckStateEx;
import com.sony.imaging.app.srctrl.shooting.state.NormalCaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.S1OffEEStateEx;
import com.sony.imaging.app.srctrl.shooting.state.S1OnEEStateEx;
import com.sony.imaging.app.srctrl.shooting.state.S1OnEEStateForTouchAF;
import com.sony.imaging.app.srctrl.shooting.state.S1OnEEStateForTouchAFAssist;
import com.sony.imaging.app.srctrl.shooting.state.SelfTimerCaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.ShootingMenuStateEx;
import com.sony.imaging.app.srctrl.shooting.state.ShootingStateEx;
import com.sony.imaging.app.util.Environment;

public class Factory extends com.sony.imaging.app.fw.Factory  {
	@Override
	protected Class<? extends StateFactory> getTop() {
		return RootStateFactory.class;
	}	

	static public class RootStateFactory extends StateFactory {
		@Override
		public void init() {			
			add(SRCtrl.SRCTRL_ROOT, SRCtrlRootState.class);
			Factory.add(SRCtrlRootState.class, ManagerStateFactory.class, null, null);
			
			add(SRCtrl.CAUTION, CautionFragment.class);
			Factory.add(CautionFragment.class, CautionStateFactory.class, null, null);
			
			add(SRCtrl.EXIT_SCREEN, ExitScreenState.class);
			Factory.add(ExitScreenState.class, null, ExitScreenLayoutFactory.class, null);
			
			add(BaseApp.FORCE_EXIT_SCREEN, ForceExitScreenState.class);
			Factory.add(ForceExitScreenState.class, null, ForceExitScreenLayoutFactory.class, null);
		}
	}

	static public class ManagerStateFactory extends StateFactory {
		@Override
		public void init() {
			add(SRCtrlRootState.APP_SHOOTING, ShootingStateEx.class);
			Factory.add(ShootingStateEx.class, ShootingStateFactory.class, ShootingLayoutFactory.class, ShootingStateKeyHandler.class);
			
			add(SRCtrlRootState.APP_NETWORK, NetworkRootState.class);
			Factory.add(NetworkRootState.class, NwStateFactory.class, null, null);
			
			add(SRCtrlRootState.FATAL_ERROR, NwFatalErrorState.class);
			Factory.add(NwFatalErrorState.class, null, NwFatalErrorLayoutFactory.class, NwFatalErrorKeyHandler.class);
		}
	}
	
	static public class CautionStateFactory extends StateFactory {
		@Override
		public void init() {
			add("CautionDisplayState", CautionDisplayStateEx.class);
			Factory.add(CautionDisplayStateEx.class, null, CautionLayoutFactory.class, null);
		}
	}

	static public class ExitScreenLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add("ExitScreen", ExitScreenLayout.class);
		}
	}

	
	static public class NwStateFactory extends StateFactory {
		@Override
		public void init(){
			add(NetworkRootState.ID_STANDBY, NwStandbyState.class);
			Factory.add(NwStandbyState.class, null, NwStandbyLayoutFactory.class, NwStandbyKeyHandler.class);
			
			add(NetworkRootState.ID_WAITING, NwWaitingState.class);
			Factory.add(NwWaitingState.class, null, NwWaitingLayoutFactory.class, NwWaitingKeyHandler.class);
			
			add(NetworkRootState.ID_CONNECTED, NwConnectedState.class);
			Factory.add(NwConnectedState.class, null, NwConnectedLayoutFactory.class, NwConnectedKeyHandler.class);
			
			add(NetworkRootState.ID_WPS_PBC, NwWpsPbcState.class);
			Factory.add(NwWpsPbcState.class, null, NwWpsPbcLayoutFactory.class, NwWpsPbcKeyHandler.class);
			
			add(NetworkRootState.ID_WPS_PIN_PRINT, NwWpsPinPrintState.class);
			Factory.add(NwWpsPinPrintState.class, null, NwWpsPinPrintLayoutFactory.class, NwWpsPinPrintKeyHandler.class);
			
			add(NetworkRootState.ID_WPS_PIN_INPUT, NwWpsPinInputState.class);
			Factory.add(NwWpsPinInputState.class, null, NwWpsPinInputLayoutFactory.class, NwWpsPinInputKeyHandler.class);
			
			add(NetworkRootState.ID_REGISTERING, NwWpsRegisteringState.class);
			Factory.add(NwWpsRegisteringState.class, null, NwWpsRegisteringLayoutFactory.class, NwWpsRegisteringKeyHandler.class);
			
			add(NetworkRootState.ID_WPS_ERROR, NwWpsErrorState.class);
			Factory.add(NwWpsErrorState.class, null, NwWpsErrorLayoutFactory.class, NwWpsErrorKeyHandler.class);

			add(NetworkRootState.ID_CONFIRM, NwConfirmationState.class);
			Factory.add(NwConfirmationState.class, null, NwConfirmChangeConfigLayoutFactory.class, NwConfirmationKeyHandler.class);
			
			add(NetworkRootState.ID_RESTARTING, RestartingState.class);
			Factory.add(RestartingState.class, null, NwRestartingLayoutFactory.class, RestartingKeyHandler.class);
		}
	}
	
	
	static public class NwStandbyLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwStandbyState.ID_STANDBY_LAYOUT, NwStandbyLayout.class);
		}
	}
	
	static public class NwWaitingLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwWaitingState.ID_WAITING_LAYOUT, NwWaitingLayout.class);
		}
	}
	
	static public class NwConnectedLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwConnectedState.ID_CONNECTED_LAYOUT, NwConnectedLayout.class);
		}
	}
	
	static public class NwWpsPbcLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwWpsPbcState.ID_PBC_LAYOUT, NwWpsPbcLayout.class);
		}
	}
	
	static public class NwWpsPinPrintLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwWpsPinPrintState.ID_PIN_PRINT_LAYOUT, NwWpsPinPrintLayout.class);
		}
	}
	
	static public class NwWpsPinInputLayoutFactory extends LayoutFactory {
		@Override
		public void init(){
			add(NwWpsPinInputState.ID_PIN_INPUT_LAYOUT, NwWpsPinInputLayout.class);
		}
	}
	
	static public class NwWpsRegisteringLayoutFactory extends LayoutFactory {
		@Override
		public void init(){
			add(NwWpsRegisteringState.ID_REGISTERING_LAYOUT, NwWpsRegisteringLayout.class);
		}
	}
	
	static public class NwWpsErrorLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwWpsErrorState.ID_INVALID_LAYOUT, NwWpsInvalidLayout.class);
			add(NwWpsErrorState.ID_TIMEOUT_LAYOUT, NwWpsTimeoutLayout.class);
		}
	}
	
	static public class NwFatalErrorLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwFatalErrorState.ID_FATAL_LAYOUT, NwFatalErrorLayout.class);
		}
	}
	
	static public class NwConfirmChangeConfigLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NwConfirmationState.ID_CONFIRM_CHANGE_CONFIG_LAYOUT, NwConfirmationLayout.class);
		}
	}
	
	static public class NwRestartingLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(RestartingState.ID_CHANGING_CONFIG_LAYOUT, RestartingLayout.class);
		}
	}
	
	static public class ShootingStateFactory extends StateFactory {
		@Override
		public void init() {
			add("FORCESETTING", SRCtrlForceSettingState.class);
			Factory.add(SRCtrlForceSettingState.class, null, null, null);
	
			add("ExposureModeCheck", ExposureModeCheckStateEx.class);
			Factory.add(ExposureModeCheckStateEx.class, null, null,	null);			
			
			add("EE", EEStateEx.class);
			Factory.add(EEStateEx.class, EEStateFactory.class, null, EEStateKeyHandler.class);

			add(CaptureStateEx.STATE_NAME, CaptureStateEx.class);
			Factory.add(CaptureStateEx.class, CaptureStateFactory.class, null, CaptureStateKeyHandlerEx.class);
			
			add("Development", DevelopmentStateEx.class);
			Factory.add(DevelopmentStateEx.class, DevelopmentStateFactory.class, DevelopmentLayoutFactory.class, DevelopmentStateKeyHandlerEx.class);

			add("AutoReview", AutoReviewState.class);
			Factory.add(AutoReviewState.class, null, AutoReviewLayoutFactory.class, AutoReviewStateKeyHandlerEx.class);

			add("AutoReviewForLimitedContShooting", AutoReviewForLimitedContShootingState.class);
			Factory.add(AutoReviewForLimitedContShootingState.class, null, AutoReviewForLimitedContShootingLayoutFactory.class, AutoReviewForLimitedContShootingStateKeyHandler.class);

			add("CustomWhiteBalance", CustomWhiteBalanceControllerStateEx.class);
			Factory.add(CustomWhiteBalanceControllerStateEx.class, CustomWhiteBalanceControllerStateFactory.class,null,  null);

			if (Environment.isMovieAPISupported()) {
				add("MovieCapture", MovieCaptureState.class);
				Factory.add(MovieCaptureState.class, null, MovieLayoutFactory.class, MovieCaptureStateKeyHandler.class);
				add("MovieRec", MovieRecState.class);
				Factory.add(MovieRecState.class, MovieRecStateFactory.class, MovieLayoutFactory.class, MovieRecStateKeyHandler.class);
				add("MovieSave", MovieSaveState.class);
				Factory.add(MovieSaveState.class, null, MovieSaveLayoutFactory.class, MovieSaveStateKeyHandler.class);
			}
		}
	}

	static public class MovieRecStateFactory extends StateFactory {
		@Override
		public void init() {
			add("Menu", MovieMenuState.class);
			Factory.add(MovieMenuState.class, null, MenuLayoutFactory.class, null);
		}
	}

	static public class MovieLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add("DefaultLayout", SRCtrlStableLayout.class);
			add(DigitalZoomSettingLayout.MENU_ID, DigitalZoomSettingLayout.class);
			add(MovieStateBase.FOCUS_LAYOUT, FocusLayout.class);			
		}
	}

	static public class MovieSaveLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add("MovieSaveLayout", MovieSaveLayout.class);
		}
	}

	static public class EEStateFactory extends StateFactory {
		@Override
		public void init() {			
			add(S1OffEEStateEx.STATE_NAME, S1OffEEStateEx.class);
			Factory.add(S1OffEEStateEx.class, null, S1OffEELayoutFactory.class, S1OffEEStateKeyHandlerEx.class);

            add(S1OnEEStateEx.STATE_NAME, S1OnEEStateEx.class);
            Factory.add(S1OnEEStateEx.class, null, S1OnEELayoutFactory.class, S1OnEEStateKeyHandler.class);

    // @tama 要確認
            add(S1OnEEStateForTouchAF.STATE_NAME, S1OnEEStateForTouchAF.class);
            Factory.add(S1OnEEStateForTouchAF.class, null, S1OnEELayoutFactory.class, S1OnEEStateKeyHandlerForTouchAF.class);

            add(S1OnEEStateForTouchAFAssist.STATE_NAME, S1OnEEStateForTouchAFAssist.class);
            Factory.add(S1OnEEStateForTouchAFAssist.class, null, MfAssistLayoutFactory.class, S1OnEEStateKeyHandlerForTouchAFAssist.class);

			add("MfAssist", MfAssistState.class);
			Factory.add(MfAssistState.class, null, MfAssistLayoutFactory.class, MfAssistKeyHandlerEx.class);
			
			add("FocusAdjustment", FocusAdjustmentState.class);
			Factory.add(FocusAdjustmentState.class, null, FocusAdjustmentFactory.class, FocusAdjustmentKeyHandler.class);
			
			add("MovieRecStandby", MovieRecStandbyState.class);
			Factory.add(MovieRecStandbyState.class, null, MovieLayoutFactory.class, MovieRecStandbyStateKeyHandler.class);

			add("Menu", ShootingMenuStateEx.class);
			Factory.add(ShootingMenuStateEx.class, null, MenuLayoutFactory.class, null);		
		}
	}
	
	static public class CaptureStateFactory extends StateFactory {
		@Override
		public void init() {
			add("NormalCapture", NormalCaptureStateEx.class);
			Factory.add(NormalCaptureStateEx.class, null, null, NormalCaptureStateKeyHandlerEx.class);

			add("SelfTimerCapture", SelfTimerCaptureStateEx.class);
			Factory.add(SelfTimerCaptureStateEx.class, null, SelfTimerCaptureLayoutFactory.class, SelfTimerCaptureStateKeyHandler.class);
		}
	}

	static public class DevelopmentStateFactory extends StateFactory {
		@Override
		public void init() {
		}
	}

	static public class CustomWhiteBalanceControllerStateFactory extends StateFactory {
		@Override
		public void init() {
			add("CWBEE", CustomWhiteBalanceEEStateEx.class);
			Factory.add(CustomWhiteBalanceEEStateEx.class, null, CWBEELayoutFactory.class, CustomWhiteBalanceEEKeyHandler.class);

			add("CWBCapture", CustomWhiteBalanceCaptureStateEx.class);
			Factory.add(CustomWhiteBalanceCaptureStateEx.class, null, null, CustomWhiteBalanceCaptureKeyHandler.class);

			add("CWBExposure", CustomWhiteBalanceExposureStateEx.class);
			Factory.add(CustomWhiteBalanceExposureStateEx.class, null, CWBExposureLayoutFactory.class, CustomWhiteBalanceExposureKeyHandler.class);
		}
	}

    // @tama 要確認　→ExitScreenLayoutFactory いらない？

	static public class ForceExitScreenLayoutFactory extends LayoutFactory {
	    @Override
	    public void init() {
	        add("ForceExitScreen", ForceExitScreenLayout.class);
	    }
	}

	static public class CautionLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add("cautionLayout", CautionLayout.class);
		}
	}

	static public class ShootingLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(ShootingState.DEFAULT_LAYOUT, ShootingLayout.class);
		}
	}
	
	/*
	static public class CaptureLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(CaptureState.PROGRESS_LAYOUT, ProgressLayout.class);
		}
	}
	*/
	
	/**
	 * the LayoutFactory which CapturingState has.
	 * @see LayoutFactory
	 */
	static public class DevelopmentLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(DevelopmentStateEx.DEFAULT_LAYOUT, ProgressLayout.class);
		}
	}

	/**
	 * the LayoutFactory which AutoReviewState has.
	 * @see LayoutFactory
	 */
	static public class AutoReviewLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(AutoReviewState.DEFAULT_LAYOUT, AutoReviewLayoutEx.class);
		}
	}

	/**
	 * the LayoutFactory which AutoReviewForLimitedContShootingState has.
	 * @see LayoutFactory
	 */
	static public class AutoReviewForLimitedContShootingLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(AutoReviewForLimitedContShootingState.DEFAULT_LAYOUT, AutoReviewForLimitedContShootingLayout.class);
 		}
 	}

	/**
	 * the LayoutFactory which make Layouts EEState has.
	 * @see LayoutFactory
	 */
	static public class S1OffEELayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(S1OffEEState.DEFAULT_LAYOUT, SRCtrlStableLayout.class);
			add(S1OffEEState.FOCUS_LAYOUT, FocusLayout.class);
			add(S1OffEEState.GUIDE_LAYOUT, GuideLayout.class);
			add(S1OffEEState.S1OFF_LAYOUT, S1OffLayout.class);  // @tama いるかも？
			add(S1OffEEStateEx.WIFI_ICON_LAYOUT, WifiIconLayout.class);
		}
	}

	/**
	 * the LayoutFactory which make Layouts LockedState has.
	 * @see LayoutFactory
	 */
	static public class S1OnEELayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(S1OnEEState.DEFAULT_LAYOUT, SRCtrlStableLayout.class);
			add(S1OnEEState.FOCUS_LAYOUT, FocusLayout.class);
			add(S1OnEEStateEx.WIFI_ICON_LAYOUT, WifiIconLayout.class);
		}
	}

    /**
     * the LayoutFactory which make Layouts MfAssistState has.
     * @see LayoutFactory
     */
    static public class MfAssistLayoutFactory extends LayoutFactory {
            @Override
            public void init() {
                    add(MfAssistState.DEFAULT_LAYOUT, MfAssistLayout.class);
            }
    }

	/**
	 * the LayoutFactory which make Layouts FocusAdjustmentState has.
	 * @see LayoutFactory
	 */
	static public class FocusAdjustmentFactory extends LayoutFactory {
		@Override
		public void init() {
			add(FocusAdjustmentState.DEFAULT_LAYOUT, FocusAdjustmentLayout.class);
		}
	}

    /**
	 * the LayoutFactory which make Layouts EEState has.
	 *
	 * @see LayoutFactory
	 */
	static public class CWBEELayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(CustomWhiteBalanceEEState.DEFAULT_LAYOUT, CustomWhiteBalanceEELayout.class);
		}
	}

	/**
	 * the LayoutFactory which CapturingState has.
	 * @see LayoutFactory
	 */
	static public class NormalCaptureLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(NormalCaptureState.DEFAULT_LAYOUT, SRCtrlStableLayout.class);
			add(NormalCaptureState.FOCUS_LAYOUT, FocusLayout.class);
		}
	}

	/**
	 * the LayoutFactory which CapturingState has.
	 * @see LayoutFactory
	 */
	static public class SelfTimerCaptureLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(SelfTimerCaptureState.DEFAULT_LAYOUT, SRCtrlStableLayout.class);
			add(SelfTimerCaptureState.FOCUS_LAYOUT, FocusLayout.class);
		}
	}

	/**
	 * the LayoutFactory which make Layouts EEState has.
	 *
	 * @see LayoutFacotry
	 */
	static public class CWBExposureLayoutFactory extends LayoutFactory {
		@Override
		public void init() {
			add(CustomWhiteBalanceExposureState.DEFAULT_LAYOUT, CustomWhiteBalanceConfLayout.class);
		}
	}

	static public class MenuLayoutFactory extends LayoutFactory{
		@Override
		public void init() {
			Class<? extends LayoutFactory> layoutFactory = OneAnotherMenuLayoutFactory.class;
			add("ID_WHITEBALANCEMENULAYOUT", WhiteBalanceMenuLayout.class);
			Factory.add(WhiteBalanceMenuLayout.class, null, layoutFactory, null);

			add("ID_WHITEBALANCEADJUSTMENTMENULAYOUT", WhiteBalanceAdjustmentMenuLayout.class);
			Factory.add(WhiteBalanceAdjustmentMenuLayout.class, null, layoutFactory, null);

			add("ID_FLASHCOMPENSATIONMENULAYOUT", FlashCompensationMenuLayout.class);
			Factory.add(FlashCompensationMenuLayout.class, null, layoutFactory, null);

			add("ID_AFFLEXIBLEPOSITIONMENULAYOUT", AFFlexiblePositionMenuLayout.class);
			Factory.add(AFFlexiblePositionMenuLayout.class, null, layoutFactory, null);

			add("ID_CREATIVESTYLEMENULAYOUT", CreativeStyleMenuLayout.class);
			Factory.add(CreativeStyleMenuLayout.class, null, layoutFactory, null);

			add("ID_AFLOCALPOSITIONMENULAYOUT", AFLocalPositionMenuLayout.class);
			Factory.add(AFLocalPositionMenuLayout.class, null, layoutFactory, null);

			add("ID_EXPOSUREMODEMENULAYOUT", ExposureModeMenuLayout.class);
			Factory.add(ExposureModeMenuLayout.class, null, layoutFactory, null);

			add("ID_SCENESELECTIONMENULAYOUT", SceneSelectionMenuLayout.class);
			Factory.add(SceneSelectionMenuLayout.class, null, layoutFactory, null);

            add("ID_EXPOSURECOMPENSATIONMENULAYOUT", ExposureCompensationMenuLayout.class);
            Factory.add(ExposureCompensationMenuLayout.class, null, layoutFactory, null);

			add("ID_MOVIEMODEMENULAYOUT", MovieModeMenuLayout.class);
			Factory.add(MovieModeMenuLayout.class, null, layoutFactory, null);

            add(UnknownItemMenuLayout.MENU_ID, UnknownItemMenuLayout.class);
            Factory.add(UnknownItemMenuLayout.class, null, layoutFactory, null);

            add("LastBastionLayout", LastBastionMenuLayoutEx.class);
            Factory.add(LastBastionMenuLayoutEx.class, null, layoutFactory, null);
                        
            add(FnMenuLayout.MENU_ID, FnMenuLayout.class);
            Factory.add(FnMenuLayout.class, null, layoutFactory, null);
            
            add(Fn15LayerMenuLayout.MENU_ID, Fn15LayerMenuLayout.class);
            Factory.add(Fn15LayerMenuLayout.class, null, layoutFactory, null);
                                    
            add(SpecialScreenMenuLayout.MENU_ID, SpecialScreenMenuLayout.class);
            Factory.add(SpecialScreenMenuLayout.class, null, layoutFactory, null);
            
            add(ISOSpecialScreenMenuLayout.MENU_ID, ISOSpecialScreenMenuLayout.class);
            Factory.add(ISOSpecialScreenMenuLayout.class, null, layoutFactory, null);
            
            add(DigitalZoomSettingLayout.MENU_ID, DigitalZoomSettingLayout.class);
            Factory.add(DigitalZoomSettingLayout.class, null, layoutFactory, null);

            add(FocusModeSpecialScreenMenuLayout.MENU_ID, FocusModeSpecialScreenMenuLayout.class);
            Factory.add(FocusModeSpecialScreenMenuLayout.class, null, layoutFactory, null);            
            
            /* FnMenu 1.5Layer Exposure Compensation */
            add(Fn15LayerExposureCompensationLayout.MENU_ID, Fn15LayerExposureCompensationLayout.class);
            Factory.add(Fn15LayerExposureCompensationLayout.class, null, layoutFactory, null);

            /* FnMenu 1.5Layer Flash Compensation */
            add(Fn15LayerFlashCompensationLayout.MENU_ID, Fn15LayerFlashCompensationLayout.class);
            Factory.add(Fn15LayerFlashCompensationLayout.class, null, layoutFactory, null);

            /* FnMenu 1.5Layer Flash Compensation */
            add(Fn15LayerCreativeStyleLayout.MENU_ID, Fn15LayerCreativeStyleLayout.class);
            Factory.add(Fn15LayerCreativeStyleLayout.class, null, layoutFactory, null);
            
            /* FnMenu 1.5Layer WhiteBalance */
            add(Fn15LayerWhiteBalanceLayout.MENU_ID, Fn15LayerWhiteBalanceLayout.class);
            Factory.add(Fn15LayerWhiteBalanceLayout.class, null, layoutFactory, null);
            
            /* FnMenu 1.5Layer ISO */
            add(ISOFn15LayerMenuLayout.MENU_ID, ISOFn15LayerMenuLayout.class);
            Factory.add(ISOFn15LayerMenuLayout.class, null, layoutFactory, null);
            
            add(PageMenuLayout.MENU_ID, PageMenuLayout.class);
            Factory.add(PageMenuLayout.class, null, layoutFactory, null);
            
            add(SetMenuLayout.MENU_ID, SetMenuLayout.class);
            Factory.add(SetMenuLayout.class, null, layoutFactory, null);
            
            add(SpecialScreenMenuLayoutHiddenEE.MENU_ID, SpecialScreenMenuLayoutHiddenEE.class);
            Factory.add(SpecialScreenMenuLayoutHiddenEE.class, null, layoutFactory, null);
		}
	}

/*	static public class MenuLayoutFactory extends LayoutFactory{
		@Override
		public void init() {
			Class<? extends LayoutFactory> layoutFactory = OneAnotherMenuLayoutFactory.class;
			add("ID_GLOBALMENULAYOUT", GlobalMenuLayout.class);
			Factory.add(GlobalMenuLayout.class, null, layoutFactory, null);

			add("ID_SUBMENULAYOUT", SubMenuLayout.class);
			Factory.add(SubMenuLayout.class, null, layoutFactory, null);

			add("ID_DOUBLELAYERMAINMENULAYOUT", DoubleLayerMainMenuLayout.class);
			Factory.add(DoubleLayerMainMenuLayout.class, null, layoutFactory, null);
			
			add("ID_DOUBLELAYERSUBMENULAYOUT", DoubleLayerSubMenuLayout.class);
			Factory.add(DoubleLayerSubMenuLayout.class, null, layoutFactory, null);

			add("ID_DOUBLELAYERWHITEBALANCESUBMENULAYOUT", DoubleLayerWhiteBalanceSubMenuLayout.class);
			Factory.add(DoubleLayerWhiteBalanceSubMenuLayout.class, null, layoutFactory, null);
			
			add("ID_DOUBLELAYERCREATIVESTYLEMENULAYOUT", DoubleLayerCreativeStyleMenuLayout.class);
			Factory.add(DoubleLayerCreativeStyleMenuLayout.class, null, layoutFactory, null);
			
			add("ID_DUMMYSUBMENULAYOUT", DummySubMenuLayout.class);
			Factory.add(DummySubMenuLayout.class, null, layoutFactory, null);
			
			add("ID_OPTIONMENULAYOUT", OptionMenuLayout.class);
			Factory.add(OptionMenuLayout.class, null, layoutFactory, null);

			add("ID_WHITEBALANCESUBMENULAYOUT", WhiteBalanceSubMenuLayout.class);
			Factory.add(WhiteBalanceSubMenuLayout.class, null, layoutFactory, null);

			add("ID_WHITEBALANCEOPTIONMENULAYOUT", WhiteBalanceOptionMenuLayout.class);
			Factory.add(WhiteBalanceOptionMenuLayout.class, null, layoutFactory, null);

			add("ID_FLASHOPTIONMENULAYOUT", FlashOptionMenuLayout.class);
			Factory.add(FlashOptionMenuLayout.class, null, layoutFactory, null);

			add("ID_AFFLEXIBLEPOSITIONMENULAYOUT", AFFlexiblePositionMenuLayout.class);
			Factory.add(AFFlexiblePositionMenuLayout.class, null, layoutFactory, null);

			add("ID_CREATIVESTYLEMENULAYOUT", CreativeStyleMenuLayout.class);
			Factory.add(CreativeStyleMenuLayout.class, null, layoutFactory, null);

			add("ID_AFLOCALPOSITIONMENULAYOUT", AFLocalPositionMenuLayout.class);
			Factory.add(AFLocalPositionMenuLayout.class, null, layoutFactory, null);

			add("ID_EXPOSUREMODESUBMENULAYOUT", ExposureModeSubMenuLayoutEx.class);
			Factory.add(ExposureModeSubMenuLayoutEx.class, null, layoutFactory, null);

			add("ID_SCENESELECTIONOPTIONMENULAYOUT", SceneSelectionOptionMenuLayout.class);
			Factory.add(SceneSelectionOptionMenuLayout.class, null, layoutFactory, null);

            add("ID_EXPOSURECOMPENSATIONOPTIONMENULAYOUT", ExposureCompensationOptionMenuLayout.class);
            Factory.add(ExposureCompensationOptionMenuLayout.class, null, layoutFactory, null);
            
            add("ID_CUSTOMFNMENULAYOUT", CustomFnMenuLayout.class);
            Factory.add(CustomFnMenuLayout.class, null, layoutFactory, null);       
            
            add("ID_FOCUSAREALAYOUT", DoubleFocusAreaSubMenuLayout.class);
            Factory.add(DoubleFocusAreaSubMenuLayout.class, null, layoutFactory, null);
            
            add(UnknownItemMenuLayout.MENU_ID, UnknownItemMenuLayout.class);
            Factory.add(UnknownItemMenuLayout.class, null, layoutFactory, null);  
		}
	}
*/
	static public class OneAnotherMenuLayoutFactory extends LayoutFactory{
		@Override
		public void init() {
			add("ID_WHITEBALANCEMENULAYOUT", WhiteBalanceMenuLayout.class);
			add("ID_WHITEBALANCEADJUSTMENTMENULAYOUT", WhiteBalanceAdjustmentMenuLayout.class);
			add("ID_FLASHCOMPENSATIONMENULAYOUT", FlashCompensationMenuLayout.class);
			add("ID_AFFLEXIBLEPOSITIONMENULAYOUT", AFFlexiblePositionMenuLayout.class);
			add("ID_CREATIVESTYLEMENULAYOUT", CreativeStyleMenuLayout.class);
			add("ID_AFLOCALPOSITIONMENULAYOUT", AFLocalPositionMenuLayout.class);
			add("ID_EXPOSUREMODEMENULAYOUT", ExposureModeMenuLayout.class);
			add("ID_SCENESELECTIONMENULAYOUT", SceneSelectionMenuLayout.class);
            add("ID_EXPOSURECOMPENSATIONMENULAYOUT", ExposureCompensationMenuLayout.class);
			add("ID_MOVIEMODEMENULAYOUT", MovieModeMenuLayout.class);
            add(UnknownItemMenuLayout.MENU_ID, UnknownItemMenuLayout.class);
            add("LastBastionLayoutEx", LastBastionMenuLayoutEx.class);
            add(FnMenuLayout.MENU_ID, FnMenuLayout.class);
            add(Fn15LayerMenuLayout.MENU_ID, Fn15LayerMenuLayout.class);
            add(SpecialScreenMenuLayout.MENU_ID, SpecialScreenMenuLayout.class);
            add(ISOSpecialScreenMenuLayout.MENU_ID, ISOSpecialScreenMenuLayout.class);
            add(DigitalZoomSettingLayout.MENU_ID, DigitalZoomSettingLayout.class);
            add(FocusModeSpecialScreenMenuLayout.MENU_ID, FocusModeSpecialScreenMenuLayout.class);
            /* FnMenu 1.5Layer Exposure Compensation */
            add(Fn15LayerExposureCompensationLayout.MENU_ID, Fn15LayerExposureCompensationLayout.class);
            /* FnMenu 1.5Layer Flash Compensation */
            add(Fn15LayerFlashCompensationLayout.MENU_ID, Fn15LayerFlashCompensationLayout.class);
            add(Fn15LayerCreativeStyleLayout.MENU_ID, Fn15LayerCreativeStyleLayout.class);
            /* FnMenu 1.5Layer WhiteBalance */
            add(Fn15LayerWhiteBalanceLayout.MENU_ID, Fn15LayerWhiteBalanceLayout.class);
            add(ISOFn15LayerMenuLayout.MENU_ID, ISOFn15LayerMenuLayout.class);
            add(PageMenuLayout.MENU_ID, PageMenuLayout.class);
            add(SetMenuLayout.MENU_ID, SetMenuLayout.class);
            add(SpecialScreenMenuLayoutHiddenEE.MENU_ID, SpecialScreenMenuLayoutHiddenEE.class);
		}
	}

	/*static public class OneAnotherMenuLayoutFactory extends LayoutFactory{
		@Override
		public void init() {
			add("ID_GLOBALMENULAYOUT", GlobalMenuLayout.class);
			add("ID_SUBMENULAYOUT", SubMenuLayout.class);
			add("ID_DOUBLELAYERMAINMENULAYOUT", DoubleLayerMainMenuLayout.class);
			add("ID_DOUBLELAYERSUBMENULAYOUT", DoubleLayerSubMenuLayout.class);
			add("ID_DOUBLELAYERWHITEBALANCESUBMENULAYOUT", DoubleLayerWhiteBalanceSubMenuLayout.class);			
			add("ID_DOUBLELAYERCREATIVESTYLEMENULAYOUT", DoubleLayerCreativeStyleMenuLayout.class);
			add("ID_DUMMYSUBMENULAYOUT", DummySubMenuLayout.class);
			add("ID_OPTIONMENULAYOUT", OptionMenuLayout.class);
			add("ID_WHITEBALANCESUBMENULAYOUT", WhiteBalanceSubMenuLayout.class);
			add("ID_WHITEBALANCEOPTIONMENULAYOUT", WhiteBalanceOptionMenuLayout.class);
			add("ID_FLASHOPTIONMENULAYOUT", FlashOptionMenuLayout.class);
			add("ID_AFFLEXIBLEPOSITIONMENULAYOUT", AFFlexiblePositionMenuLayout.class);
			add("ID_CREATIVESTYLEMENULAYOUT", CreativeStyleMenuLayout.class);
			add("ID_AFLOCALPOSITIONMENULAYOUT", AFLocalPositionMenuLayout.class);
			add("ID_EXPOSUREMODESUBMENULAYOUT", ExposureModeSubMenuLayoutEx.class);
			add("ID_SCENESELECTIONOPTIONMENULAYOUT", SceneSelectionOptionMenuLayout.class);
            add("ID_EXPOSURECOMPENSATIONOPTIONMENULAYOUT", ExposureCompensationOptionMenuLayout.class);
            add("ID_CUSTOMFNMENULAYOUT", CustomFnMenuLayout.class);
            add("ID_FOCUSAREALAYOUT", DoubleFocusAreaSubMenuLayout.class);
            add(UnknownItemMenuLayout.MENU_ID, UnknownItemMenuLayout.class);
		}
	}
	*/
}
