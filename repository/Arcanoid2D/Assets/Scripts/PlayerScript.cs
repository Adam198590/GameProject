using UnityEngine;
using System.Collections;

public class PlayerScript : MonoBehaviour {
	public float playerVelocity;
	public float boundary;
	private Vector3 playerPosition;

	private int playerLives;
	private int playerPoints;

	public AudioClip pointSound;
	public AudioClip lifeSound;

	private GUIStyle guiStyle;

	// Use this for initialization
	void Start () {
		playerPosition = transform.position;
		playerLives = 3;
		playerPoints = 0;
		guiStyle = new GUIStyle();
		guiStyle.fontSize = 20;
		guiStyle.fontStyle = FontStyle.Bold;
		guiStyle.normal.textColor = Color.blue;
	}
	
	// Update is called once per frame
	void Update () {
		playerPosition.x += Input.GetAxis("Horizontal") * playerVelocity;
		transform.position = playerPosition;

		if (playerPosition.x < -boundary) {
//			transform.position = new Vector3(-boundary, playerPosition.y, playerPosition.z);
			playerPosition.x = -boundary;
		}

		if (playerPosition.x > boundary) {
//			transform.position = new Vector3(boundary, playerPosition.y, playerPosition.z);
			playerPosition.x = boundary;
		}

		if (Input.GetKeyDown(KeyCode.Escape)) {
			Application.Quit();
		}

		WinLose();
	}

	void addPoints(int points) {
		playerPoints += points;
		GetComponent<AudioSource>().PlayOneShot (pointSound);
	}

	void TakeLife() {
		playerLives--;
		GetComponent<AudioSource>().PlayOneShot (lifeSound);
	}

	void OnGUI() {
		GUI.Label (new Rect(5.0f, 3.0f, 400.0f, 400.0f), 
		           "Live's: " + playerLives + "  Score: " + playerPoints, 
		           guiStyle);
	}

	void WinLose() {
		if (playerLives == 0) {
			playerLives = 3;
			Application.LoadLevel("Level1");
		}

		if ((GameObject.FindGameObjectsWithTag ("Block")).Length == 0) {
			if (Application.loadedLevelName.Equals("Level1")) {
				Application.LoadLevel("Level2");
			} else {
				Application.Quit();
			}
		}
	}
}