//
//  GalleryViewController.swift
//  BeautyGallery
//
//  Created by Vobile on 6/16/15.
//  Copyright (c) 2015 silence. All rights reserved.
//

import UIKit

class GalleryViewController: UIViewController {
    
    var imageName: String?
    @IBOutlet weak var beatyImage: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        if imageName != nil {
            beatyImage.image = UIImage(named: imageName!)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}
