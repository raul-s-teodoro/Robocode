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
		
		
		// Verifica a distância antes de atirar
		if (e.getDistance() < 220) {
			// Se estiver perto (menos de 220 pixels), atira com força 3
			fire(3);
		} else {
			// Se estiver longe, atira com força 1
			fire(1);
		}
		

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

	/**Vai até o centro da arena quando bate na parede*/
	public void onHitWall(robocode.HitWallEvent e) {
		
		// 1. Pega as coordenadas do centro da arena
		double meioX = getBattleFieldWidth() / 2;
		double meioY = getBattleFieldHeight() / 2;
		
		// 2. Pega as coordenadas atuais do robô
		double meuX = getX();
		double meuY = getY();
		
		// 3. Calcula a distância (delta) em X e Y
		double deltaX = meioX - meuX;
		double deltaY = meioY - meuY;
		
		// 4. Calcula o ângulo absoluto para o centro (em radianos)
		//    (Usamos atan2 para calcular o ângulo a partir das distâncias)
		double anguloParaCentroRad = Math.atan2(deltaX, deltaY);
		
		// 5. Converte o ângulo para graus (que o Robocode usa)
		double anguloParaCentroDeg = Math.toDegrees(anguloParaCentroRad);
		
		// 6. Calcula quanto o robô precisa virar
		//    (Ângulo para onde queremos ir) - (Ângulo para onde estamos olhando)
		double anguloDeGiro = anguloParaCentroDeg - getHeading();

		// 7. Vira o CORPO do robô pelo caminho mais curto
		turnRight(Utils.normalRelativeAngleDegrees(anguloDeGiro));
		
		// 8. Calcula a distância até o centro
		double distanciaParaCentro = Math.hypot(deltaX, deltaY);
		
		// 9. Anda até o centro
		//    Este comando INTERROMPE o 'run()' e faz o robô
		//    ir direto para o meio antes de continuar o 'seesaw'.
		ahead(distanciaParaCentro);
	}
}												

