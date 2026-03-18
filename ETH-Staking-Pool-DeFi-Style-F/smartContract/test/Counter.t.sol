// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "forge-std/Test.sol";
import "../src/Counter.sol";

contract CounterTest is Test {
    Counter counter;
    address user = address(0xABCD);
    uint256 rewardRate = 1 ether; // 1 ETH per second for testing

    function setUp() public {
        // Deploy the Counter contract with rewardRate
        counter = new Counter(rewardRate);

        // Fund the user address with ETH
        vm.deal(user, 10 ether);
    }

    function testStake() public {
        // Simulate user calling stake() with 1 ether
        vm.startPrank(user);
        counter.stake{value: 1 ether}();
        vm.stopPrank();

        // Check stored stake
        (uint256 amount, uint256 startTime, bool claimed) = counter.stakes(user);
        assertEq(amount, 1 ether);
        assertFalse(claimed);
        assertGt(startTime, 0);
    }

    function testCalculateReward() public {
        vm.startPrank(user);
        counter.stake{value: 1 ether}();
        vm.warp(block.timestamp + 5); // simulate 5 seconds later
        uint256 reward = counter.calculateReward(user);
        vm.stopPrank();

        // Reward = duration * rewardRate = 5 * 1 ether = 5 ether
        assertEq(reward, 5 ether);
    }

    function testUnstake() public {
        vm.startPrank(user);
        counter.stake{value: 1 ether}();
        vm.warp(block.timestamp + 5); // simulate 5 seconds
        uint256 balanceBefore = user.balance;

        vm.deal(address(counter), 10 ether); // fund contract to pay rewards

        counter.unstake();

        uint256 balanceAfter = user.balance;
        uint256 reward = 5 ether; // 5 seconds * 1 ether

        // Check that user received stake + reward
        assertEq(balanceAfter - balanceBefore, 1 ether + reward);

        // Stake should be marked as claimed
        (, , bool claimed) = counter.stakes(user);
        assertTrue(claimed);

        // Further reward should be 0
        assertEq(counter.calculateReward(user), 0);

        vm.stopPrank();
    }

    function testCannotUnstakeTwice() public {
        vm.startPrank(user);
        counter.stake{value: 1 ether}();
        counter.unstake();

        // Second unstake should revert
        vm.expectRevert("Stake already claimed");
        counter.unstake();
        vm.stopPrank();
    }

    function testCannotStakeZero() public {
        vm.startPrank(user);
        vm.expectRevert("Must stake more than 0");
        counter.stake{value: 0}();
        vm.stopPrank();
    }
}