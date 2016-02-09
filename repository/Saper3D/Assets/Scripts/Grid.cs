using UnityEngine;
using System.Collections;

public class Grid : MonoBehaviour {
	public GameObject tilePrefab;
	public int numberOfTiles = 10;
	public int tilesPerRow = 4;
	public float distanceBetweenTiles = 1.0f;

	// Use this for initialization
	void Start () {
		CreateTiles();
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	private void CreateTiles() {
		float xOffset = 0.0f;
		float zOffset = 0.0f;

		for (int tilesCreated = 0; tilesCreated < numberOfTiles; tilesCreated++) {
			xOffset += distanceBetweenTiles;

			if (tilesCreated % tilesPerRow == 0) {
				zOffset += distanceBetweenTiles;
				xOffset = 0;
			}

			Instantiate(
				tilePrefab, 
				new Vector3(
					transform.position.x + xOffset, 
					transform.position.y, 
					transform.position.z + zOffset),
				transform.rotation);
		}
	}
}