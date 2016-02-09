using UnityEngine;
using System.Collections;

public class BlockScript : MonoBehaviour {

	public int hitsToKill;
	public int points;
	private int numberOfHits;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void OnCollisionEnter2D(Collision2D collision){
		if (collision.gameObject.tag.Equals("Ball")){
			numberOfHits++;

			if (numberOfHits == hitsToKill){
				GameObject player = GameObject.FindGameObjectsWithTag("Player")[0];
				player.SendMessage("addPoints", points);

				Destroy(this.gameObject);
			}
		}
	}


}