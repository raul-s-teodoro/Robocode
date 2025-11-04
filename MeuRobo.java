/*
 * Copyright (c) 2001-2025 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package sample;


import robocode.HitByBulletEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;


/**
 * MyFirstRobot - a sample robot by Mathew Nelson.
 * <p>
 * Moves in a seesaw motion, and spins the gun around at each end.
 *
 * @author Mathew A. Nelson (original)
 */
public class MeuRobo extends Robot {

	/**
	 * MyFirstRobot's run method - Seesaw
	 */
	public void run() {

		while (true) {
			ahead(100); // Move ahead 100
			turnGunRight(360); // Spin gun around
			back(100); // Move back 100
			turnGunRight(360); // Spin gun around
		}
	}

	/**
	 * Fire when we see a robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		/**Calcula o ângulo para virar o canhao
		 * usa a posição do robo (getHeading), a posição do inimigo (e.getBearing) 
		 * e a posição atual do canhão (getGunHeading).
		 */
		double giroDoCanhao = (getHeading() + e.getBearing()) - getGunHeading();

		// faz o canhao virar para o caminho mais curto
		double anguloNormalizado = Utils.normalRelativeAngleDegrees(giroDoCanhao);

		// interrome o 'turnGunRight(360)' do método 'run'.
		turnGunRight(anguloNormalizado);
		fire(1);
		

		/** 
		 * se um inimigo for scanneado, o robo atira e continua com o canhao virado para onde ele atirou
		 * até que o inimigo saia da área scanneada
		 * quando isso acontece, a arma gira 360 graus de novo para procurar inimigos
		 */
	}

	/**
	 * We were hit!  Turn perpendicular to the bullet,
	 * so our seesaw might avoid a future shot.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}

	/**Dá meia volta quando bate na parede */
	public void onHitWall(robocode.HitWallEvent e) {
		back(30);
		turnRight(180);
		ahead(80);
	}
}												

