package com.sony.imaging.app.srctrl.shooting.keyhandler;

import com.sony.imaging.app.base.shooting.trigger.AutoReviewStateKeyHandler;

/**
 * 
 * Make invalid all keys in AutoReviewState.
 * @author 0000138134
 *
 */
public class AutoReviewStateKeyHandlerEx extends AutoReviewStateKeyHandler {
	@Override
	public int pushedUpKey() {
		return INVALID;
	}

	@Override
	public int pushedDownKey() {
		return INVALID;
	}

	@Override
	public int pushedCenterKey() {
		return INVALID;
	}

	@Override
	public int pushedSK2Key() {
		return INVALID;
	}

	@Override
	public int turnedShuttleToLeft() {
		return INVALID;
	}

	@Override
	public int turnedShuttleToRight() {
		return INVALID;
	}

	@Override
	public int turnedDial1ToLeft() {
		return INVALID;
	}

	@Override
	public int turnedDial1ToRight() {
		return INVALID;
	}

	@Override
	public int turnedDial2ToLeft() {
		return INVALID;
	}

	@Override
	public int turnedDial2ToRight() {
		return INVALID;
	}
	

	

	/////////////////////////////////////////////////////////////
	// Invalidate moveTo***() function in the super class.
    @Override
    public int pushedDispFuncKey() {
        return INVALID;
    }
	@Override
    public int pushedPbZoomFuncMinus() {
        return INVALID;
    }
    @Override
    public int turnedMainDialNext() {
        return INVALID;
    }
    @Override
    public int turnedMainDialPrev() {
        return INVALID;
    }
    public int pushedPbZoomFuncPlus() {
        return INVALID;
    }
    @Override
    public int pushedLeftKey() {
        return INVALID;
    }
    @Override
    public int pushedRightKey() {
        return INVALID;
    }
    @Override
    public int pushedPlayBackKey() {
        return INVALID;
    }
    public int pushedDeleteFuncKey() {
        return INVALID;
    }
    @Override
    public int turnedSubDialNext() {
        return INVALID;
    }
    @Override
    public int turnedSubDialPrev() {
        return INVALID;
    }
    @Override
    public int pushedS2Key() {
        return INVALID;
    }
    @Override
    public int pushedUmRemoteS2Key()
    {
        return INVALID;
    }
}
