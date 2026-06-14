"use client"

import HeaderComponent from "@/components/HeaderComponent"
import FooterComponent from "@/components/FooterComponent"

export default function Saude(){
    return (
        <div className="flex justify-center items-center bg-[#F0F0F0]">
                <main className="h-full w-[85%]">
                    <HeaderComponent />
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8 ">
                      <div className="flex flex-col gap-8">
                      </div>
                    </div>
                    <FooterComponent />
                </main>
              <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
        </div>
    )
}