// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract StakingPool {
    mapping(address => uint256) public stakes;
    uint256 public totalStaked;
    bool private locked;

    /// @notice Emitted when a user stakes ETH into the pool.
    /// @param user The address of the user who staked.
    /// @param amount The amount of ETH staked.
    event Staked(address indexed user, uint256 amount);

    /// @notice Emitted when a user unstakes ETH from the pool.
    /// @param user The address of the user who unstaked.
    /// @param amount The amount of ETH unstaked.
    event Unstaked(address indexed user, uint256 amount);

    /// @notice Prevents reentrancy attacks by locking the contract during execution.
    modifier nonReentrant() {
        require(!locked, "Reentrant call");
        locked = true;
        _;
        locked = false;
    }

    /// @notice Allows a user to stake ETH into the contract.
    /// @dev The user must send a positive amount of ETH along with the transaction.
    function stake() external payable {
        require(msg.value > 0, "Must stake positive amount");
        stakes[msg.sender] += msg.value;
        totalStaked += msg.value;
        emit Staked(msg.sender, msg.value);
    }

    /// @notice Allows a user to withdraw their staked ETH.
    /// @dev Protects against reentrancy attacks. Reverts if balance is insufficient.
    /// @param amount The amount of ETH to unstake.
    function unstake(uint256 amount) external nonReentrant {
        require(stakes[msg.sender] >= amount, "Insufficient stake");
        stakes[msg.sender] -= amount;
        totalStaked -= amount;
        payable(msg.sender).transfer(amount);
        emit Unstaked(msg.sender, amount);
    }

    function getStake(address user) external view returns (uint256) {
        return stakes[user];
    }
}