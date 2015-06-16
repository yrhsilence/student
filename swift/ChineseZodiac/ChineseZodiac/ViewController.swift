//
//  ViewController.swift
//  ChineseZodiac
//
//  Created by Vobile on 6/12/15.
//  Copyright (c) 2015 silence. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var yearOfBirth: UITextField!
    @IBOutlet weak var image: UIImageView!
    let offset = 4
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    override func touchesEnded(touches: Set<NSObject>, withEvent event: UIEvent) { // 点击空白区域
        yearOfBirth.resignFirstResponder()
    }
    
    @IBAction func okTapped(sender: AnyObject) {
        yearOfBirth.resignFirstResponder() // 隐藏number pad
        
        
        if let year = yearOfBirth.text.toInt() {
            var imageNumber = (year - offset) % 12
            image.image = UIImage(named: String(imageNumber))
        } else {
            // notify the user
        }
        
    }
}

