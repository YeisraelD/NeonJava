// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;
contract Counter {
    struct Stake {
        uint256 amount;
        uint256 startTime;
        bool claimed;
    }

    address public owner;
    
    uint256 public rewardRate;

    mapping(address => Stake) public stakes; // user address to their stake details
    constructor(uint256 _rewardRate) {
        owner = msg.sender;
        rewardRate = _rewardRate;
    }

    function stake() external payable {
        require(msg.value > 0, "Must stake more than 0");
        stakes[msg.sender] = Stake({
            amount: msg.value,
            startTime: block.timestamp,
            claimed: false
        });
    }

    function calculateReward(address user) public view returns (uint256) {
        Stake memory userStake = stakes[user];
        if (userStake.claimed) {
            return 0; // No reward if already claimed
        }
        uint256 stakingDuration = block.timestamp - userStake.startTime;
        uint256 reward = stakingDuration * rewardRate;
        return reward;
    }
    function unstake() external {
        Stake storage userStake = stakes[msg.sender];
        require(!userStake.claimed, "Stake already claimed");
        uint256 reward = calculateReward(msg.sender);
        userStake.claimed = true;
        payable(msg.sender).call{value: userStake.amount + reward}("");
        

    }

}

