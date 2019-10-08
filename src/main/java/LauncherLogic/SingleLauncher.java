package LauncherLogic;

import java.io.Serializable;

public class SingleLauncher implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final byte banksAmmount;
	private final static int channelsInBank = 8;
	private boolean[][] banksState;
	
	private final long identifier;
	private final int chanel;
	
	public SingleLauncher(byte banksAmmount, int chanel, long identifier) {
		this.banksAmmount = banksAmmount;
		this.identifier = identifier;
		this.chanel = chanel;
		
		banksState = new boolean[this.banksAmmount][SingleLauncher.channelsInBank];
	}
	
	public void setSingleChanelState(int bankNumber, int chanelInBankNumber, boolean state) {
		if ((bankNumber>0) && (chanelInBankNumber>0) && 
				(bankNumber<this.banksAmmount) && (chanelInBankNumber<SingleLauncher.channelsInBank)) {
			banksState[bankNumber][chanelInBankNumber] = state;
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
		
		if (((SingleLauncher) obj).getChanel() != this.getChanel()) {
			return false;
		}
		
		if (((SingleLauncher) obj).getIdentifier() != this.getIdentifier()) {
			return false;
		}
		
		return true;
		
	}
	
	
}
