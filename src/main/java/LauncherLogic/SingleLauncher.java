package LauncherLogic;

import java.io.Serializable;

/**
 * 
 * @author S.S.S.
 * 
 * This is class for storing data about condition of one launching module
 *
 */
public class SingleLauncher implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 *  Ammount of launcher boards. Number of channels would be x8 
	 */
	private final byte banksAmmount;
	
	/**
	 * Ammount of channels on every bank
	 */
	private final static int channelsInBank = 8;
	
	/**
	 * Current state of launcher
	 */
	private boolean[][] banksState;
	
	/**
	 * Maximal state of launcher, used for detection of channel fails
	 */
	private boolean[][] maxBanksState;
	
	/**
	 * Desired state of launcher, uploaded from launch plan, used for check-up
	 */
	private boolean[][] desiredBanksState;
	
	/**
	 * 8-bit identifier
	 */
	private final long identifier;
	
	/**
	 * RF channel number, used for rf connection
	 */
	private final int chanel;
	
	/**
	 * RF node name, used for rf connection
	 */
	private final String node;
	
	public SingleLauncher(byte banksAmmount, int chanel, long identifier, String node) {
		this.banksAmmount = banksAmmount;
		this.identifier = identifier;
		this.chanel = chanel;
		this.node = node;
		
		this.banksState = new boolean[this.banksAmmount][SingleLauncher.channelsInBank];
		this.maxBanksState = new boolean[this.banksAmmount][SingleLauncher.channelsInBank];
		this.desiredBanksState = new boolean[this.banksAmmount][SingleLauncher.channelsInBank];
	}
	
	public String getNode() {
		return node;
	}

	public void setSingleChanelState(int bankNumber, int chanelInBankNumber, boolean state) {
		if ((bankNumber>0) && (chanelInBankNumber>0) && 
				(bankNumber<this.banksAmmount) && (chanelInBankNumber<SingleLauncher.channelsInBank)) {
			banksState[bankNumber][chanelInBankNumber] = state;
			maxBanksState[bankNumber][chanelInBankNumber] = maxBanksState[bankNumber][chanelInBankNumber] || state;
		} else {
			throw new IndexOutOfBoundsException("No such bank or chanel in bank!");
		}
	}
	
	public boolean getSingleChanelState(int bankNumber, int chanelInBankNumber) {
		if ( (bankNumber<this.banksAmmount) && (chanelInBankNumber<SingleLauncher.channelsInBank)) {
			return banksState[bankNumber][chanelInBankNumber];
		} else {
			throw new IndexOutOfBoundsException("No such bank or chanel in bank!");
		}
	}
	
	public boolean getMaxSingleChanelState(int bankNumber, int chanelInBankNumber) {
		if ( (bankNumber<this.banksAmmount) && (chanelInBankNumber<SingleLauncher.channelsInBank)) {
			return maxBanksState[bankNumber][chanelInBankNumber];
		} else {
			throw new IndexOutOfBoundsException("No such bank or chanel in bank!");
		}
	}
	
	public boolean getDesiredSingleChanelState(int bankNumber, int chanelInBankNumber) {
		if ( (bankNumber<this.banksAmmount) && (chanelInBankNumber<SingleLauncher.channelsInBank)) {
			return desiredBanksState[bankNumber][chanelInBankNumber];
		} else {
			throw new IndexOutOfBoundsException("No such bank or chanel in bank!");
		}
	}
	
	public void setState(boolean[][] state) {
		if (state.length == this.banksAmmount) {
			for (boolean[] localBank : state) {
				if (localBank.length != SingleLauncher.channelsInBank) {
					throw new IndexOutOfBoundsException("State of bank mismatch!");
				}
			}
			
			for (int i = 0; i < this.banksAmmount; i++) {
				for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
					this.banksState[i][j] = state[i][j];
					this.maxBanksState[i][j] = this.maxBanksState[i][j] || state[i][j];
				}
			}
		} else {
			throw new IndexOutOfBoundsException("State size mismatch launcher!");
		}
	}
	
	public void setState(boolean[] state) {
		if (state.length != this.banksAmmount*SingleLauncher.channelsInBank) {
			throw new IndexOutOfBoundsException("State size mismatch launcher!");
		}
		
		for (int i = 0; i < this.banksAmmount; i++) {
			for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
				this.banksState[i][j] = state[i*SingleLauncher.channelsInBank + j];
				this.maxBanksState[i][j] = this.maxBanksState[i][j] || state[i*SingleLauncher.channelsInBank + j];
			}
		}
	}
	
	public void setDesiredState(boolean[][] state) {
		if (state.length == this.banksAmmount) {
			for (boolean[] localBank : state) {
				if (localBank.length != SingleLauncher.channelsInBank) {
					throw new IndexOutOfBoundsException("State of bank mismatch!");
				}
			}
			
			for (int i = 0; i < this.banksAmmount; i++) {
				for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
					this.desiredBanksState[i][j] = state[i][j];
				}
			}
		} else {
			throw new IndexOutOfBoundsException("State size mismatch launcher!");
		}
	}
	
	public void setDesiredState(boolean[] state) {
		if (state.length != this.banksAmmount*SingleLauncher.channelsInBank) {
			throw new IndexOutOfBoundsException("State size mismatch launcher!");
		}
		
		for (int i = 0; i < this.banksAmmount; i++) {
			for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
				this.desiredBanksState[i][j] = state[i*SingleLauncher.channelsInBank + j];
			}
		}
	}

	public byte getBanksAmmount() {
		return banksAmmount;
	}

	public long getIdentifier() {
		return identifier;
	}

	public int getChanel() {
		return chanel;
	}
	
	public boolean compareState(SingleLauncher launcher) {
		if (launcher.getBanksAmmount() != this.getBanksAmmount()) {
			return false;
		}
		
		for (int i = 0; i < this.banksAmmount; i++) {
			for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
				if (launcher.getSingleChanelState(i, j) != this.banksState[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof SingleLauncher) ) {
			return false;
		}
		
		if (obj==this) {
			return true;
		}
		
		if (((SingleLauncher) obj).getBanksAmmount() != this.getBanksAmmount()) {
			return false;
		}
		
		for (int i = 0; i < this.banksAmmount; i++) {
			for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
				if (((SingleLauncher) obj).getSingleChanelState(i, j) != this.banksState[i][j]) {
					return false;
				}
			}
		}
		
		for (int i = 0; i < this.banksAmmount; i++) {
			for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
				if (((SingleLauncher) obj).getMaxSingleChanelState(i, j) != this.maxBanksState[i][j]) {
					return false;
				}
			}
		}
		
		for (int i = 0; i < this.banksAmmount; i++) {
			for (int j = 0; j < SingleLauncher.channelsInBank; j++) {
				if (((SingleLauncher) obj).getDesiredSingleChanelState(i, j) != this.desiredBanksState[i][j]) {
					return false;
				}
			}
		}
		
		if (((SingleLauncher) obj).getChanel() != this.getChanel()) {
			return false;
		}
		
		if (((SingleLauncher) obj).getIdentifier() != this.getIdentifier()) {
			return false;
		}
		
		return true;
		
	}
	
	
}
