#include <libintech/uart.hpp>
#include <libintech/timer.hpp>
#include <stdint.h>
#include <avr/interrupt.h>
#include <util/delay.h>
#include "balise.h"
#define IGNORE_TIMER0_OVF_vect
#define IGNORE_TIMER1_OVF_vect
#define IGNORE_TIMER2_OVF_vect
#define IGNORE_INT2_vect
#define IGNORE_PCINT2_vect
#include <libintech/interrupt_manager.hpp>
#include <libintech/isr.hpp>

INITIALISE_INTERRUPT_MANAGER();

int main() 
{
    Balise &balise = Balise::Instance();
    
    while(1)
    {
        char buffer[100];
        Balise::serial_pc::read(buffer);
        balise.execute(buffer);
    }
}


ISR(TIMER0_OVF_vect)
{

}

/**
 * Interruption timer top tour, ne se produit pas quand le moteur tourne suffisament vite
 * 
 */
ISR(TIMER1_OVF_vect)
{
	Balise &balise = Balise::Instance();
		
    // Remise à zéro de la vitesse	
    balise.last_period(0);
		
    // Désactivation du timer	
    Balise::timer_toptour::disable();	
    Balise::timer_toptour::counter::value(0);
}

/**
 * Interruption pour l'asservissement du moteur
 * 
 */
ISR(TIMER2_OVF_vect)
{
	static int32_t previous_encoder = 0;
	Balise &balise = Balise::Instance();
    Balise::Instance().control(balise.encoder - previous_encoder);
    previous_encoder = balise.encoder;
}

/**
 * Interruption capteur top tour (aimant passant devant le capteur)
 * 
 */
ISR(INT2_vect)
{
    Balise &balise = Balise::Instance();
    
    // On ignore les impulsions quand l'aimant est encore trop proche du capteur
    if (Balise::timer_toptour::counter::value() >= balise.last_period() / 3)
    {
        balise.last_period(Balise::timer_toptour::counter::value());
        Balise::timer_toptour::counter::value(0);
        Balise::timer_toptour::enable();	
    }
}

/**
 * Interruption des codeuses
 * 
 */
ISR(PCINT2_vect)
{
    // TODO: intégrer la lib codeuse
	static uint8_t canal_a;
	static uint8_t canal_b;
	static uint8_t previous_canal_a;
	static uint8_t previous_canal_b;
	
	Balise &balise = Balise::Instance();
	
	canal_a = C1::read();
	canal_b = C0::read();
	
	// Vérification que l'on est sur un front (useless ?)
	if (!(canal_a != previous_canal_a || canal_b != previous_canal_b)) return;
	
	bool sens = canal_a == previous_canal_b;
	
	// Incrémente ou décrémente en fonction du sens
	if (sens)
	{
		balise.encoder++;
	}
	else
	{
		balise.encoder--;
	}
	
	previous_canal_a = canal_a;
	previous_canal_b = canal_b;
}

